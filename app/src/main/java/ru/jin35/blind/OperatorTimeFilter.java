package ru.jin35.blind;

import android.os.SystemClock;
import rx.Observable;
import rx.Subscriber;

public class OperatorTimeFilter<T> implements Observable.Operator<T, T> {

    private final long timeout;
    private long lastEmitted = 0;
    private final TimeGetter timeGetter;

    public OperatorTimeFilter(long timeout) {
        this.timeout = timeout;
        timeGetter = new TimeGetter() {
            @Override
            public long getTime() {
                return SystemClock.uptimeMillis();
            }
        };
    }

    OperatorTimeFilter(TimeGetter timeGetter, long timeout) {
        this.timeGetter = timeGetter;
        this.timeout = timeout;
    }

    @Override
    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        return new Subscriber<T>() {
            @Override
            public void onCompleted() {
                child.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                child.onError(e);
            }

            @Override
            public void onNext(T t) {
                long now = timeGetter.getTime();
                if (now - lastEmitted < timeout) {
                    request(1);
                    return;
                }
                child.onNext(t);
                lastEmitted = now;
            }
        };
    }

    /**
     * For tests
     */
    interface TimeGetter {
        long getTime();
    }
}

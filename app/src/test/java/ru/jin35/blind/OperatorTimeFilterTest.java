package ru.jin35.blind;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observers.TestSubscriber;

import java.util.concurrent.TimeUnit;

@RunWith(RobolectricGradleTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = 21
)
public class OperatorTimeFilterTest {

    private Observable<Integer> intervalScan;
    private OperatorTimeFilter.TimeGetter timeGetter;

    @Before
    public void setUp() throws Exception {
        intervalScan = Observable
                .interval(0, 100, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long aLong) {
                        return 1;
                    }
                })
                .scan(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer, Integer integer2) {
                        return integer + integer2;
                    }
                });

        timeGetter = new OperatorTimeFilter.TimeGetter() {
            @Override
            public long getTime() {
                return System.currentTimeMillis();
            }
        };
    }

    @Test
    public void testValidSetup() throws Exception {
        TestSubscriber<Integer> subscriber = TestSubscriber.create();
        intervalScan
                .subscribe(subscriber);
        subscriber.awaitTerminalEvent(350, TimeUnit.MILLISECONDS);
        subscriber.assertValues(1, 2, 3, 4);
    }

    @Test
    public void testFilter() throws Exception {
        TestSubscriber<Integer> subscriber1 = TestSubscriber.create();
        intervalScan
                .lift(new OperatorTimeFilter<Integer>(timeGetter, 150))
                .subscribe(subscriber1);
        subscriber1.awaitTerminalEvent(350, TimeUnit.MILLISECONDS);
        subscriber1.assertValues(1, 3);
    }
}
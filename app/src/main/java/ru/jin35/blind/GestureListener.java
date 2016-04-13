package ru.jin35.blind;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import rx.Observable;
import rx.subjects.PublishSubject;

public class GestureListener extends FrameLayout {

    private final GestureDetector gestureDetector;
    private final PublishSubject<Move> gesturesObservable;

    public GestureListener(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, new InternalGestureListener());
        if (!isInEditMode()) {
            gesturesObservable = PublishSubject.create();
        } else {
            gesturesObservable = null;
        }
    }

    public Observable<Move> getGesturesObservable() {
        return gesturesObservable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private class InternalGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                gesturesObservable.onNext(velocityX > 0 ? Move.RIGHT : Move.LEFT);
            } else {
                gesturesObservable.onNext(velocityY > 0 ? Move.DOWN : Move.UP);
            }

            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }
}

package ru.jin35.blind;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;

public class Utils {

    public static void addOnPreDrawListener(@Nullable View target, @NonNull ViewTreeObserver.OnPreDrawListener l) {
        if (target == null) {
            return;
        }
        ViewTreeObserver obs = target.getViewTreeObserver();
        if (obs == null) {
            return;
        }
        obs.addOnPreDrawListener(l);
    }

    public static void removeOnPreDrawListener(@Nullable View target, @NonNull ViewTreeObserver.OnPreDrawListener l) {
        if (target == null) {
            return;
        }
        ViewTreeObserver obs = target.getViewTreeObserver();
        if (obs == null) {
            return;
        }
        obs.removeOnPreDrawListener(l);
    }

    public static void executeBeforeDraw(@Nullable final View target, final Runnable animation) {
        addOnPreDrawListener(target, new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                animation.run();
                removeOnPreDrawListener(target, this);
                return true;
            }
        });
    }
}

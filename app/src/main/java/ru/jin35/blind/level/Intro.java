package ru.jin35.blind.level;

import android.support.annotation.Nullable;
import ru.jin35.blind.Map;

import static ru.jin35.blind.Map.F;
import static ru.jin35.blind.Map.L;
import static ru.jin35.blind.Map.S;
import static ru.jin35.blind.Map.W;

public class Intro extends Level {

    private static final Map MAP = new Map(new int[][]{
            new int[]{W, W, W, W, W, W, W},
            new int[]{W, S | L, 0, L, 0, F | L, W},
            new int[]{W, W, W, W, W, W, W},
    });

    public Intro(int id, @Nullable Integer nextId) {
        super(id, nextId, MAP);
    }
}

package ru.jin35.blind.level;

import android.support.annotation.Nullable;
import ru.jin35.blind.Map;

import static ru.jin35.blind.Map.W;

public class Big extends Level {

    private static final Map MAP = new Map(new int[][]{
            new int[]{W, W, W, W, W, W, W, W, W, W, W, W, W},
            new int[]{W, B, W, X, 0, 0, 0, 0, 0, 0, 0, 0, W},
            new int[]{W, 0, W, W, W, 0, W, W, W, W, W, 0, W},
            new int[]{W, 0, 0, 0, W, 0, W, 0, 0, 0, W, 0, W},
            new int[]{W, 0, W, W, W, W, W, 0, W, W, W, 0, W},
            new int[]{W, 0, 0, 0, 0, 0, 0, 0, W, 0, 0, 0, W},
            new int[]{W, W, W, 0, W, W, W, W, W, 0, W, 0, W},
            new int[]{W, 0, 0, 0, 0, 0, 0, 0, 0, 0, W, 0, W},
            new int[]{W, W, W, W, W, W, W, W, W, W, W, W, W},
    });

    public Big(int id, @Nullable Integer nextId) {
        super(id, nextId, MAP);
    }
}

package ru.jin35.blind.level;

import android.support.annotation.Nullable;
import ru.jin35.blind.Map;

import static ru.jin35.blind.Map.*;

public abstract class Level {

    static final int X = F | L;
    static final int B = S | L;

    private final int id;
    @Nullable
    private final Integer nextId;
    private final Map map;

    public Level(int id, @Nullable Integer nextId, Map map) {
        this.id = id;
        this.nextId = nextId;
        this.map = map;
    }

    @Nullable
    public Integer getNextId() {
        return nextId;
    }

    public int getId() {
        return id;
    }

    public Map getMap() {
        return map;
    }
}

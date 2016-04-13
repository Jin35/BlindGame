package ru.jin35.blind;

public enum Move {
    UP(1, -2), RIGHT(0, 2), DOWN(1, 2), LEFT(0, -2);

    private final int positionIndex;
    private final int changeCount;

    Move(int positionIndex, int changeCount) {
        this.positionIndex = positionIndex;
        this.changeCount = changeCount;
    }

    public void changePosition(Map map) {
        map.position[positionIndex] += changeCount;
    }
}

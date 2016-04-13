package ru.jin35.blind;

import rx.functions.Func1;

import java.util.Arrays;

class ValidFilter implements Func1<Move, Boolean> {
    private final boolean valid;
    private final Map map;

    public ValidFilter(boolean valid, Map map) {
        this.valid = valid;
        this.map = map;
    }

    @Override
    public Boolean call(Move move) {
        int[] position = map.position;
        int[] gotoPosition = Arrays.copyOf(position, 2);
        switch (move) {
            case UP:
                gotoPosition[1]--;
                break;
            case DOWN:
                gotoPosition[1]++;
                break;
            case LEFT:
                gotoPosition[0]--;
                break;
            case RIGHT:
                gotoPosition[0]++;
                break;
        }
        boolean gotoWall = map.data[gotoPosition[1]][gotoPosition[0]] == Map.W;
        return valid != gotoWall;
    }
}

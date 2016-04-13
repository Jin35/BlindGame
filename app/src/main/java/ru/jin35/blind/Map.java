package ru.jin35.blind;

public class Map {

    public static final int W = 1;
    public static final int S = 2;
    public static final int F = 4;
    public static final int L = 8;

    final int[][] data;
    int[] position;

    public Map(int[][] data) {
        this.data = data;
        setupStartPosition();
        validateData();
    }

    private void setupStartPosition() {
        position = new int[2];
        for (int i = 1; i < data.length; i = i + 2) {
            for (int j = 1; j < data[i].length; j = j + 2) {
                if ((data[i][j] & S) != 0) {
                    position[0] = i;
                    position[1] = j;
                    return;
                }
            }
        }
        throw new IllegalArgumentException("There is no valid start position!");
    }

    private void validateData() {
        if (data.length < 3 || data[0].length < 3) {
            throw new IllegalArgumentException("Map is too small");
        }
        if (data.length % 2 != 1 || data[0].length % 2 != 1) {
            throw new IllegalArgumentException("Map should have odd size");
        }
    }

    public boolean hasWall(int x, int y) {
        return hasValue(x, y, W);
    }

    private boolean hasValue(int x, int y, int value) {
        return (data[x][y] & value) != 0;
    }

    public boolean hasLight(int x, int y) {
        return hasValue(x, y, L);
    }

    public boolean hasFinish(int x, int y) {
        return hasValue(x, y, F);
    }

    public void getFinishPosition(int[] finishPosition) {
        for (int i = 1; i < data.length; i += 2) {
            int[] row = data[i];
            for (int j = 1; j < row.length; j += 2) {
                if ((data[i][j] & F) != 0) {
                    finishPosition[0] = i;
                    finishPosition[1] = j;
                    return;
                }
            }
        }
        throw new IllegalArgumentException("There is no valid finish position!");
    }

    public boolean hasFinish(int[] position) {
        return hasFinish(position[1], position[0]);
    }
}

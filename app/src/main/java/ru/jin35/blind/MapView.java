package ru.jin35.blind;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import rx.Observable;
import rx.subjects.BehaviorSubject;

public class MapView extends View {

    private static final int CELL_TO_WALL = 3;

    private Map map;
    private Rect wallRect = new Rect();
    private Paint wallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int cellSize;
    private int wallSize;
    private int translateX;
    private int translateY;

    private BehaviorSubject<int[]> measureObservable = BehaviorSubject.create(new int[4]);

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        wallPaint.setColor(Color.GRAY);
        if (isInEditMode()) {
            setMap(LevelFactory.getLevel(context).getMap());
        }
    }

    public void setMap(Map map) {
        this.map = map;
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calcSizes();
    }

    private void calcSizes() {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        int mapWidth = map.data[0].length;
        int mapHeight = map.data.length;
        int maxCells = Math.max(mapWidth, mapHeight);

        int minMeasureDimen = Math.min(measuredWidth, measuredHeight);

        int cellCount = (maxCells - 1) / 2;
        int wallCount = cellCount + 1;
        wallSize = minMeasureDimen / (wallCount + cellCount * CELL_TO_WALL);
        cellSize = wallSize * CELL_TO_WALL;

        int cellWidthCount = (mapWidth - 1) / 2;
        int contentWidth = (cellWidthCount + 1) * wallSize + cellWidthCount * cellSize;
        translateX = (measuredWidth - contentWidth) / 2;

        int cellHeightCount = (mapHeight - 1) / 2;
        int contentHeight = (cellHeightCount + 1) * wallSize + cellHeightCount * cellSize;
        translateY = (measuredHeight - contentHeight) / 2;
        measureObservable.onNext(new int[]{cellSize, wallSize, translateX, translateY});
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.save();
        canvas.translate(translateX, translateY);
        int left;
        int right;
        int top = 0;
        int bottom = wallSize;
        for (int i = 0; i < map.data.length; i++) {
            int[] row = map.data[i];
            left = 0;
            right = wallSize;
            for (int j = 0; j < row.length; j++) {
                if (map.hasWall(i, j)) {
                    wallRect.set(left, top, right, bottom);
                    canvas.drawRect(wallRect, wallPaint);
                }
                //values for next item
                left = right;
                right += (j % 2 == 0) ? cellSize : wallSize;
            }
            //values for next row
            top = bottom;
            bottom += (i % 2 == 0) ? cellSize : wallSize;
        }
        canvas.restore();
    }

    public Observable<int[]> getObservable() {
        return measureObservable;
    }
}

package ru.jin35.blind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class LightView extends View {

    private static final int NO_LIGHT_COLOR = Color.BLACK;

    private int cellSize;
    private int wallSize;
    private Map map;

    private int translateX;
    private int translateY;
    private final Paint maskPaint;
    private final Bitmap gradient;
    private final Rect src = new Rect();
    private final Rect dest = new Rect();

    public LightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        gradient = BitmapFactory.decodeResource(context.getResources(), R.drawable.gradient);
        src.set(0, 0, gradient.getWidth(), gradient.getHeight());
        if (isInEditMode()) {
            setMap(LevelFactory.getLevel(context).getMap());
        }
    }

    public void setMap(Map map) {
        this.map = map;
        invalidate();
    }

    public void setSizes(int cellSize, int wallSize, int translateX, int translateY) {
        this.cellSize = cellSize;
        this.wallSize = wallSize;
        this.translateX = translateX;
        this.translateY = translateY;

        int lightSize = (int) (cellSize + wallSize * 1.5);
        int bigCellSize = cellSize + wallSize*2;
        int diff = bigCellSize - lightSize;
        int diffHalf = diff / 2;
        dest.set(diffHalf, diffHalf, bigCellSize - diffHalf, bigCellSize - diffHalf);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas) {
        int saveCount = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.drawColor(NO_LIGHT_COLOR);
        canvas.translate(translateX, translateY);
        for (int i = 1; i < map.data.length; i = i + 2) {
            int[] row = map.data[i];
            for (int j = 1; j < row.length; j = j + 2) {
                if (map.hasLight(i, j)) {
                    int dx = ((j - 1) / 2) * (cellSize + wallSize);
                    int dy = ((i - 1) / 2) * (cellSize + wallSize);
                    dest.offset(dx, dy);
                    canvas.drawBitmap(gradient, src, dest, maskPaint);
                    dest.offset(-dx, -dy);
                }
            }
        }
        canvas.restoreToCount(saveCount);
    }
}

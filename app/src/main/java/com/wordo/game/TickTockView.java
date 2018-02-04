package com.wordo.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Arihant on 22-06-2016.
 */
public class TickTockView extends View {

    private final Paint paint = new Paint();
    private final int loaderTopColor = Color.rgb(145, 220, 90);
    private final int loaderBottomColor = Color.argb(100, 153, 204, 0);
    private long currentTime, totalTime;
    private RectF oval;

    public TickTockView(Context context) {
        super(context);
    }

    public TickTockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTime(long currentTime, long totalTime) {
        this.currentTime = currentTime;
        this.totalTime = totalTime;
        oval = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (currentTime >= 0 && oval != null) {
            int height = canvas.getHeight();
            int width = canvas.getWidth();

            float radius = width > height ? height / 2 - (20 * height / 128) : width / 2 - (20 * height / 128);
            paint.setStrokeWidth(2f);
            paint.setTextSize(30 * width / height);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(Color.BLACK);
            canvas.drawText((int) currentTime + "", width / 2, height / 2 + (12 * height / 128), paint);
            paint.setStrokeWidth((10 * width / height));
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(loaderBottomColor);
            oval.set(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius);
            canvas.drawArc(oval, 0, 360, false, paint);
            paint.setColor(loaderTopColor);
            canvas.drawArc(oval, 0, (float) ((currentTime * 360) / totalTime), false, paint);
        }
    }
}

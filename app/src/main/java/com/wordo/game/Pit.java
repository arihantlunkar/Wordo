package com.wordo.game;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Arihant on 20-06-2016.
 */
public class Pit {
    private final Paint paint = new Paint();
    public float positionX, positionY, radius;
    private BlurMaskFilter solidBlurMaskFilter;

    public Pit(float positionX, float positionY, float radius) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;
        solidBlurMaskFilter = new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID);
    }

    public void customDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setMaskFilter(solidBlurMaskFilter);
        canvas.drawCircle(positionX, positionY, radius, paint);
    }
}

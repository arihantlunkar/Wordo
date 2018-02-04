package com.wordo.game;

/**
 * Created by Arihant on 02-12-2015.
 */

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class AlphabetBall {

    private final Paint paint = new Paint();
    public Boolean isSucked;
    public float positionX, positionY, speedX, speedY, radius;
    public char spellingLetter;
    private int screenWidth;

    public AlphabetBall(float positionX, float positionY, float radius, float speed, float angleInDegree,
                        char spellingLetter, int screenWidth) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.speedX = (float) (speed * Math.cos(Math.toRadians(angleInDegree)));
        this.speedY = -speed * (float) Math.sin(Math.toRadians(angleInDegree));
        this.radius = radius;
        this.spellingLetter = spellingLetter;
        this.screenWidth = screenWidth;
        isSucked = false;
    }

    public void customDraw(Canvas canvas, Boolean isSafe) {
        paint.setTextSize(55 * screenWidth / 1080);
        paint.setAntiAlias(false);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.BLACK);
        paint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.OUTER));
        canvas.drawCircle(positionX, positionY, radius, paint);
        paint.setMaskFilter(new BlurMaskFilter(1, BlurMaskFilter.Blur.INNER));
        if (!isSafe && radius != 0)
            canvas.drawText(spellingLetter + "", positionX, positionY + 20 * screenWidth / 1080, paint);

    }
}
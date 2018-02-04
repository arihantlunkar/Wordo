package com.wordo.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Arihant on 19-06-2016.
 */
public class AngryBall {

    private final Paint paint = new Paint();
    public float positionX, positionY;
    public float speedX, speedY;
    public float radius;
    private Bitmap angryBallBitmap;

    public AngryBall(float positionX, float positionY, float radius, float speed, double angleInDegree,
                     Bitmap angryBallBitmap) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.speedX = (float) (speed * Math.cos(Math.toRadians(angleInDegree)));
        this.speedY = -speed * (float) Math.sin(Math.toRadians(angleInDegree));
        this.radius = radius;
        this.angryBallBitmap = angryBallBitmap;
    }

    public void customDraw(Canvas canvas) {
        if (angryBallBitmap != null)
            canvas.drawBitmap(angryBallBitmap, (positionX - (angryBallBitmap.getWidth() / 2)), (positionY - (angryBallBitmap.getHeight() / 2)), paint);
    }
}

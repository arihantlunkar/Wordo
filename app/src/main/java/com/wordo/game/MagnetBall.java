package com.wordo.game;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Arihant on 19-06-2016.
 */
public class MagnetBall {

    private final Paint paint = new Paint();
    public float positionX, positionY;
    public float speedX, speedY;
    public float radius;
    private Bitmap magnetBallBitmap;
    private BlurMaskFilter outerBlurMaskFilter;
    private BlurMaskFilter innerBlurMaskFilter;

    public MagnetBall(float positionX, float positionY, float radius, float speed, double angleInDegree,
                      Bitmap magnetBallBitmap) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.speedX = (float) (speed * Math.cos(Math.toRadians(angleInDegree)));
        this.speedY = -speed * (float) Math.sin(Math.toRadians(angleInDegree));
        this.radius = radius;
        this.magnetBallBitmap = magnetBallBitmap;
        innerBlurMaskFilter = new BlurMaskFilter(10, BlurMaskFilter.Blur.INNER);
        outerBlurMaskFilter = new BlurMaskFilter(10, BlurMaskFilter.Blur.OUTER);
    }

    public void customDraw(Canvas canvas) {
        if (magnetBallBitmap != null) {
            paint.setAntiAlias(false);
            paint.setColor(Color.DKGRAY);
            paint.setMaskFilter(outerBlurMaskFilter);
            canvas.drawCircle(positionX, positionY, radius, paint);
            paint.setMaskFilter(innerBlurMaskFilter);
            canvas.drawBitmap(magnetBallBitmap, (positionX - (magnetBallBitmap.getWidth() / 2)), (positionY - (magnetBallBitmap.getHeight() / 2)), paint);
        }
    }
}

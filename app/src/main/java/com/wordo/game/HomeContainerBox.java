package com.wordo.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Arihant on 19-06-2016.
 */
public class HomeContainerBox {
    private final Paint paint = new Paint();
    public float minimumX, minimumY, maximumX, maximumY;
    private Bitmap homeContainerBoxBitmap;

    public HomeContainerBox(float minimumX, float minimumY, float maximumX, float maximumY, Bitmap homeContainerBoxBitmap) {
        this.minimumX = minimumX;
        this.minimumY = minimumY;
        this.maximumX = maximumX;
        this.maximumY = maximumY;
        this.homeContainerBoxBitmap = homeContainerBoxBitmap;
    }

    public void customDraw(Canvas canvas) {
        canvas.drawBitmap(homeContainerBoxBitmap, minimumX, minimumY, paint);
    }
}

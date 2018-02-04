package com.wordo.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.wordo.R;

/**
 * Created by Arihant on 31-05-2016.
 */
public class SensorBallBitmap {

    private static Bitmap instance;
    private static int width;
    private static int height;

    public static Bitmap getInstance(Context context, int m_width, int m_height) {
        if (instance == null || width != m_width || height != m_height) {
            width = m_width;
            height = m_height;
            instance = Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(context.getResources(), R.drawable.sensorball),
                    width,
                    height,
                    true);
        }
        return instance;
    }
}

package com.wordo.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.wordo.R;

/**
 * Created by Arihant on 31-05-2016.
 */
public class HomeBitmap {

    private static final int fixedParameter = 1080;
    private static Bitmap instance;

    public static Bitmap getInstance(Context context) {
        if (instance == null) {
            instance = Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(context.getResources(), R.drawable.gameplay),
                    200 * context.getResources().getDisplayMetrics().widthPixels / fixedParameter,
                    200 * context.getResources().getDisplayMetrics().widthPixels / fixedParameter,
                    true);
        }
        return instance;
    }
}

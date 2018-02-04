package com.wordo.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


/**
 * Created by Arihant on 08-12-2015.
 */
public class SensorBall implements SensorEventListener {

    private final Paint paint = new Paint();
    public float velocityFactor, positionX, positionY;
    public Bitmap sensorBallBitmap;
    public float radius;
    private SensorManager sensorManager;
    private GameView gameViewObj;

    public SensorBall(Context context, float velocityFactor, float positionX, float positionY, Bitmap sensorBallBitmap, float radius, GameView gameViewObj) {
        this.velocityFactor = velocityFactor;
        this.sensorBallBitmap = sensorBallBitmap;
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;
        this.gameViewObj = gameViewObj;
        sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            positionX += -(event.values[0] * velocityFactor);
            positionY += (event.values[1] * velocityFactor);
            updateSensorBall();
        }
    }

    public void updateSensorBall() {
        if (gameViewObj != null && gameViewObj.isSensorBallSafe && gameViewObj.homeContainerBoxObj != null && sensorBallBitmap != null) {
            if (positionX > (gameViewObj.homeContainerBoxObj.maximumX - (sensorBallBitmap.getWidth() / 2))) {
                positionX = gameViewObj.homeContainerBoxObj.maximumX - (sensorBallBitmap.getWidth() / 2);
            } else if (positionX < (gameViewObj.homeContainerBoxObj.minimumX + (sensorBallBitmap.getWidth() / 2))) {
                positionX = gameViewObj.homeContainerBoxObj.minimumX + (sensorBallBitmap.getWidth() / 2);
            }
            if (positionY > (gameViewObj.homeContainerBoxObj.maximumY - (sensorBallBitmap.getHeight() / 2))) {
                positionY = gameViewObj.homeContainerBoxObj.maximumY - (sensorBallBitmap.getHeight() / 2);
            } else if (positionY < gameViewObj.homeContainerBoxObj.minimumY + (sensorBallBitmap.getHeight() / 2)) {
                positionY = gameViewObj.homeContainerBoxObj.minimumY + (sensorBallBitmap.getHeight() / 2);
            }
        } else if (gameViewObj != null && !gameViewObj.isSensorBallSafe && gameViewObj.roomObj != null && sensorBallBitmap != null) {
            if (positionX > (gameViewObj.roomObj.maximumX - (sensorBallBitmap.getWidth() / 2))) {
                positionX = gameViewObj.roomObj.maximumX - (sensorBallBitmap.getWidth() / 2);
            } else if (positionX < (gameViewObj.roomObj.minimumX + (sensorBallBitmap.getWidth() / 2))) {
                positionX = gameViewObj.roomObj.minimumX + (sensorBallBitmap.getWidth() / 2);
            }
            if (positionY > (gameViewObj.roomObj.maximumY - (sensorBallBitmap.getHeight() / 2))) {
                positionY = gameViewObj.roomObj.maximumY - (sensorBallBitmap.getHeight() / 2);
            } else if (positionY < gameViewObj.roomObj.minimumY + (sensorBallBitmap.getHeight() / 2)) {
                positionY = gameViewObj.roomObj.minimumY + (sensorBallBitmap.getHeight() / 2);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    public void stopSensor() {
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    public void customDraw(Canvas canvas) {
        if (sensorBallBitmap != null)
            canvas.drawBitmap(sensorBallBitmap, (positionX - (sensorBallBitmap.getWidth() / 2)), (positionY - (sensorBallBitmap.getHeight() / 2)), paint);
    }
}

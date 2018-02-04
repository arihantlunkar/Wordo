package com.wordo.game;

import android.content.Context;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wordo.R;

/**
 * Created by Arihant on 01-06-2016.
 */
public class GameView extends View {

    public final int EXPLOSION_SIZE = 200;
    public float roomHeight;
    public float roomWidth;
    public HomeContainerBox homeContainerBoxObj = null;
    public SensorBall sensorBallObj = null;
    public Pit[] pitObj = null;
    public AngryBall[] angryBallObj = null;
    public AlphabetBall[] alphabetBallObj = null;
    public MagnetBall[] magnetBallObj = null;
    public Room roomObj = null;
    public Collision collisionObj = null;
    public Explosion explosion;
    public Boolean isSensorBallSafe = true;
    public Boolean isGameOver = false;
    public long gameStartTime = System.currentTimeMillis();
    private MediaPlayer mpClick;
    private Context mContext;

    public GameView(Context context) {
        super(context);
        mContext = context;
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        roomHeight = canvas.getHeight();
        roomWidth = canvas.getWidth();

        if (homeContainerBoxObj != null && isSensorBallSafe)
            homeContainerBoxObj.customDraw(canvas);

        if (sensorBallObj != null) {
            sensorBallObj.customDraw(canvas);
            invalidate();
        }

        if (pitObj != null)
            for (int i = 0; i < pitObj.length && pitObj[i] != null; i++)
                pitObj[i].customDraw(canvas);

        if (angryBallObj != null)
            for (int i = 0; i < angryBallObj.length && angryBallObj[i] != null; i++)
                angryBallObj[i].customDraw(canvas);

        if (alphabetBallObj != null)
            for (int i = 0; i < alphabetBallObj.length && alphabetBallObj[i] != null; i++)
                alphabetBallObj[i].customDraw(canvas, isSensorBallSafe);

        if (magnetBallObj != null)
            for (int i = 0; i < magnetBallObj.length && magnetBallObj[i] != null; i++)
                magnetBallObj[i].customDraw(canvas);

        if (explosion != null)
            explosion.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        double eventX = event.getX();
        double eventY = event.getY();
        if (isSensorBallSafe &&
                eventX >= roomWidth / 2 - 100 * roomWidth / 1080 &&
                eventX <= roomWidth / 2 + 100 * roomWidth / 1080 &&
                eventY >= roomHeight / 2 - 100 * roomWidth / 1080 &&
                eventY <= roomHeight / 2 + 100 * roomWidth / 1080) {
            isSensorBallSafe = false;
            gameStartTime = System.currentTimeMillis();
            playClickMusic();
        }
        return true;
    }

    private void playClickMusic() {
        if (mContext != null) {
            if (mpClick != null) {
                mpClick.stop();
                mpClick.release();
                mpClick = null;
            }
            mpClick = MediaPlayer.create(mContext, R.raw.buttonclick);
            mpClick.start();
        }
    }
}

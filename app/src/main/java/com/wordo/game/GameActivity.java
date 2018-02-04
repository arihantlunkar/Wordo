package com.wordo.game;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.wordo.MainActivity;
import com.wordo.R;
import com.wordo.bitmap.AngryBallBitmap;
import com.wordo.bitmap.HomeBitmap;
import com.wordo.bitmap.MagnetBallBitmap;
import com.wordo.bitmap.SensorBallBitmap;
import com.wordo.levels.LevelActivity;

import java.util.Random;

import at.markushi.ui.CircleButton;

/**
 * Created by Arihant on 30-05-2016.
 */
public class GameActivity extends Activity implements View.OnClickListener {

    private static int screenWidth;
    private final String[] difficultyLevels = {"Amateur", "Semi Pro ", "Professional ", "World Class", "Legendary"};
    private final int fixedParameter = 1080;
    private final int UPDATE_RATE = 50;
    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        public void run() {
            new Thread() {
                public void run() {
                    try {
                        if (gameViewObj != null && !gameViewObj.isGameOver)
                            initMusic();
                        while (gameViewObj != null && (!gameViewObj.isGameOver || gameViewObj.explosion.isAlive())) {
                            setTimer();
                            checkCollision();
                            updateExplosion();
                            gameViewObj.postInvalidate();
                            try {
                                Thread.sleep(1000 / UPDATE_RATE);
                            } catch (InterruptedException ex) {
                                Log.d("Message", "Exception", ex.getCause());
                            }
                        }
                    } catch (Exception e) {
                        Log.d("Message", "Exception", e.getCause());
                    } finally {
                        final SharedPreferences.Editor editor = getSharedPreferences("wordo_data", Context.MODE_PRIVATE).edit();

                        if (gameViewObj != null && !gameViewObj.isSensorBallSafe && ((System.currentTimeMillis() - gameViewObj.gameStartTime) / 1000) >= 10 && lockType == 1) {
                            editor.putString(difficultyLevel.toUpperCase() + " Level", "new");
                            editor.apply();
                        }

                        if (event == BACK_PRESSED || event == BACK_BUTTON_PRESSED) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    playClickMusic();
                                    deInit();
                                    startActivity(new Intent(GameActivity.this, LevelActivity.class));
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                }
                            });
                        } else if (event == HOME_PRESSED) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    playClickMusic();
                                    deInit();
                                    startActivity(new Intent(GameActivity.this, MainActivity.class));
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                }
                            });
                        } else if (gameViewObj != null && gameViewObj.isGameOver) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    final Dialog dialog = new Dialog(GameActivity.this);

                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.getWindow().setBackgroundDrawableResource(R.color.color_white);
                                    dialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
                                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                    dialog.setContentView(R.layout.game_finish_item);
                                    dialog.getWindow().setLayout(GameActivity.this.getResources().getDisplayMetrics().widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.setCancelable(false);

                                    ImageView imageGameStatus = (ImageView) dialog.findViewById(R.id.imageGameStatus);
                                    TextView textGameStatus = (TextView) dialog.findViewById(R.id.textGameStatus);
                                    TextView textIndeed = (TextView) dialog.findViewById(R.id.textIndeed);
                                    TextView textOnlyPossibleWord = (TextView) dialog.findViewById(R.id.textOnlyPossibleWord);
                                    TextView textActualWord = (TextView) dialog.findViewById(R.id.textActualWord);
                                    Button btnSwitch = (Button) dialog.findViewById(R.id.btnSwitch);
                                    textGameStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35 * screenWidth / fixedParameter);
                                    textActualWord.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30 * screenWidth / fixedParameter + (15 / word.length()));

                                    if ((gameViewObj != null && !gameViewObj.isSensorBallSafe && ((System.currentTimeMillis() - gameViewObj.gameStartTime) / 1000) >= 10 || lockType != 1)) {
                                        textIndeed.setText("You could have made !");
                                        textActualWord.setText(word.toUpperCase());
                                    } else {
                                        textIndeed.setText("Word cannot be revealed !");
                                        String dummy = "";
                                        for (int i = 0; i < word.length(); i++)
                                            dummy += "*";
                                        textActualWord.setText(dummy);
                                        textOnlyPossibleWord.setText("Try one more attempt.");
                                    }


                                    int score = getSharedPreferences("wordo_data", Context.MODE_PRIVATE).getInt(getString(R.string.storedMyWordoScore), 0);

                                    switch (gameOverReason) {
                                        case 1:
                                            imageGameStatus.setBackgroundResource(R.drawable.time);
                                            textGameStatus.setText((new GameOverDictionary()).getGameOverWord(1));
                                            btnSwitch.setBackgroundResource(R.drawable.layers_tryagain_button_bg);
                                            break;
                                        case 2:
                                            imageGameStatus.setBackgroundResource(R.drawable.magnetball);
                                            imageGameStatus.getLayoutParams().height = 100;
                                            imageGameStatus.getLayoutParams().width = 100;
                                            textGameStatus.setText((new GameOverDictionary()).getGameOverWord(2));
                                            btnSwitch.setBackgroundResource(R.drawable.layers_tryagain_button_bg);
                                            break;
                                        case 3:
                                            imageGameStatus.setBackgroundResource(R.drawable.angryball);
                                            imageGameStatus.getLayoutParams().height = 100;
                                            imageGameStatus.getLayoutParams().width = 100;
                                            textGameStatus.setText((new GameOverDictionary()).getGameOverWord(3));
                                            btnSwitch.setBackgroundResource(R.drawable.layers_tryagain_button_bg);
                                            break;
                                        case 4:
                                            imageGameStatus.setBackgroundResource(R.drawable.hole);
                                            imageGameStatus.getLayoutParams().height = 100;
                                            imageGameStatus.getLayoutParams().width = 100;
                                            textGameStatus.setText((new GameOverDictionary()).getGameOverWord(4));
                                            btnSwitch.setBackgroundResource(R.drawable.layers_tryagain_button_bg);
                                            break;
                                        case 5:
                                            imageGameStatus.setBackgroundResource(R.drawable.incorrect);
                                            textGameStatus.setText((new GameOverDictionary()).getGameOverWord(5));
                                            btnSwitch.setBackgroundResource(R.drawable.layers_tryagain_button_bg);
                                            break;
                                        default:
                                            imageGameStatus.setBackgroundResource(R.drawable.correct);
                                            textGameStatus.setText((new GameOverDictionary()).getGameOverWord(6));
                                            textIndeed.setText("Yes. Indeed it is !");
                                            textActualWord.setText(word.toUpperCase());
                                            textGameStatus.setTextColor(Color.argb(255, 153, 204, 0));
                                            if (lockType == 1) {
                                                int dummy = 0;
                                                for (int i = 0; i < difficultyLevels.length; i++)
                                                    if (difficultyLevels[i].toUpperCase().equals(difficultyLevel.toUpperCase()))
                                                        dummy = i;
                                                int time = 0;
                                                if (gameViewObj != null) {
                                                    time = (gameTime - (int) ((System.currentTimeMillis() - gameViewObj.gameStartTime) / 1000));
                                                }
                                                if (time < 0)
                                                    time = 0;
                                                score = getSharedPreferences("wordo_data", Context.MODE_PRIVATE).getInt(getString(R.string.storedMyWordoScore), 0) + ((dummy + 1) * time);
                                                editor.putInt(getString(R.string.storedMyWordoScore), score);
                                                editor.putString(getString(R.string.storedQuery), "UPDATE dictionary SET no_of_attempts = '" + levelNo + "', solved = '1' WHERE id = '" + wordID + "'");
                                                editor.putString(difficultyLevel.toUpperCase() + " Level", "new");
                                                editor.apply();
                                            }
                                            break;
                                    }

                                    Toast toast = Toast.makeText(GameActivity.this, "Your Current High Score - " + score, Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.TOP, 0, 0);
                                    toast.show();

                                    dialog.show();

                                    dialog.findViewById(R.id.btnHomeOver).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Random rand = new Random();
                                                    Boolean showAd = rand.nextInt(2) == 0 ? true : false;
                                                    if (showAd) {
                                                        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                                                            mInterstitialAd.show();
                                                        } else {
                                                            playClickMusic();
                                                            deInit();
                                                            dialog.dismiss();
                                                            startActivity(new Intent(GameActivity.this, MainActivity.class));
                                                            finish();
                                                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                                        }
                                                    } else {
                                                        playClickMusic();
                                                        deInit();
                                                        dialog.dismiss();
                                                        startActivity(new Intent(GameActivity.this, MainActivity.class));
                                                        finish();
                                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                                    }
                                                }
                                            });
                                        }
                                    });

                                    dialog.findViewById(R.id.btnSwitch).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Random rand = new Random();
                                                    Boolean showAd = rand.nextInt(2) == 0 ? true : false;
                                                    if (showAd) {
                                                        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                                                            mInterstitialAd.show();
                                                        } else {
                                                            playClickMusic();
                                                            deInit();
                                                            dialog.dismiss();
                                                            startActivity(new Intent(GameActivity.this, LevelActivity.class));
                                                            finish();
                                                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                                        }
                                                    } else {
                                                        playClickMusic();
                                                        deInit();
                                                        dialog.dismiss();
                                                        startActivity(new Intent(GameActivity.this, LevelActivity.class));
                                                        finish();
                                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            }.start();
        }
    };
    private final Handler refreshHandler = new Handler();
    private final Runnable refreshRunnable = new RefreshRunnable();
    private final int REFRESH_RATE_IN_SECONDS = 2;
    public String spellingCompleted = "";
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private String word;
    private String gloss;
    private String wordID;
    private String difficultyLevel;
    private int levelNo;
    private int scoreNeeded;
    private int gameTime;
    private int noOfPits;
    private int noOfAngryBalls;
    private int noOMagnetBalls;
    private float sensorBallRadius;
    private float sensorBallVelocity;
    private int lockType;
    private GameView gameViewObj = null;
    private Rect roomRect = null;
    private TextView textWord, textScore;
    private Bitmap redBallBitmap, angryBallBitmap, magnetBallBitmap, homeBitmap;
    private MediaPlayer mpSecret, mpEngulf, mpFailure, mpClick;
    private int NONE = -1, BACK_PRESSED = 0, BACK_BUTTON_PRESSED = 1, HOME_PRESSED = 2;
    private int event = NONE;
    private int gameOverReason = 0;
    private TickTockView tickTockViewObj = null;
    private boolean firstAdReceived = false;

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Remove any pending ad refreshes.
        if (refreshHandler != null)
            refreshHandler.removeCallbacks(refreshRunnable);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!firstAdReceived && refreshHandler != null) {
            // Request a new ad immediately.
            refreshHandler.post(refreshRunnable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    //Charm - Hangs when app goes in pause state
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (gameViewObj != null && !gameViewObj.isGameOver) {
            gameViewObj.isGameOver = true;
            event = BACK_PRESSED;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_game);

        initGameView();

        getIntentData(savedInstanceState);

        setAd();

        setIntentData();

        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (gameViewObj != null && gameViewObj.roomHeight > 0 && !gameViewObj.isGameOver) {
                            setBitmap();
                            if (gameViewObj.isGameOver)
                                break;
                            initGameObjects();
                            if (gameViewObj.isGameOver)
                                break;
                            setPositions();
                            break;
                        }
                    }
                } catch (Exception e) {
                    Log.d("Message", "Exception", e.getCause());
                } finally {
                    handler.post(runnable);
                }
            }
        }.start();
    }

    private void initGameView() {
        screenWidth = getResources().getDisplayMetrics().widthPixels
                > getResources().getDisplayMetrics().heightPixels
                ? getResources().getDisplayMetrics().heightPixels :
                getResources().getDisplayMetrics().widthPixels;

        gameViewObj = (GameView) findViewById(R.id.gameView);
        tickTockViewObj = (TickTockView) findViewById(R.id.tickTockView);

        if (Build.VERSION.SDK_INT >= 11 && gameViewObj != null) {
            gameViewObj.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        CircleButton btnHint, btnHome, btnBack;
        btnHome = (CircleButton) findViewById(R.id.btnHome);
        btnHint = (CircleButton) findViewById(R.id.btnHint);
        btnBack = (CircleButton) findViewById(R.id.btnBack);

        btnHome.setOnClickListener(this);
        btnHint.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void setTimer() {
        if (tickTockViewObj != null) {
            if (gameViewObj != null && !gameViewObj.isSensorBallSafe) {
                if (gameTime - ((System.currentTimeMillis() - gameViewObj.gameStartTime) / 1000) >= 0 && !gameViewObj.isGameOver) {
                    tickTockViewObj.setTime(gameTime - ((System.currentTimeMillis() - gameViewObj.gameStartTime) / 1000), gameTime);
                } else if (gameTime - ((System.currentTimeMillis() - gameViewObj.gameStartTime) / 1000) < 0 && gameViewObj.sensorBallObj != null) {
                    if (!gameViewObj.isGameOver) {
                        if (gameViewObj.explosion == null || gameViewObj.explosion.getState() == Explosion.STATE_DEAD || gameViewObj.explosion.getState() == Explosion.STATE_ALIVE)
                            gameViewObj.explosion = new Explosion(gameViewObj.EXPLOSION_SIZE, (int) gameViewObj.sensorBallObj.positionX, (int) gameViewObj.sensorBallObj.positionY);

                        gameViewObj.sensorBallObj.positionX = Float.MAX_VALUE;
                        gameViewObj.sensorBallObj.positionY = Float.MAX_VALUE;
                        gameViewObj.sensorBallObj.stopSensor();
                    }
                    gameViewObj.isGameOver = true;
                    tickTockViewObj.setTime(0, gameTime);
                    gameOverReason = 1;
                }
            } else if (gameViewObj != null)
                tickTockViewObj.setTime(gameTime, gameTime);
            tickTockViewObj.postInvalidate();
        }
    }

    private void getIntentData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (savedInstanceState == null && intent != null) {
            word = intent.getStringExtra("word");
            wordID = intent.getStringExtra("wordID");
            gloss = intent.getStringExtra("gloss");
            difficultyLevel = intent.getStringExtra("difficultyLevel");
            levelNo = intent.getIntExtra("levelNo", 1);
            scoreNeeded = intent.getIntExtra("scoreNeeded", 1000);
            gameTime = intent.getIntExtra("gameTime", 120);
            noOfPits = intent.getIntExtra("noOfPits", 5);
            noOfAngryBalls = intent.getIntExtra("noOfAngryBalls", 5);
            noOMagnetBalls = intent.getIntExtra("noOfMagnetBalls", 5);
            sensorBallRadius = 35 + (50 * getSharedPreferences("wordo_data", Context.MODE_PRIVATE).getInt(getString(R.string.storedBallRadius), 50) / 100);
            sensorBallVelocity = 2 + (5 * getSharedPreferences("wordo_data", Context.MODE_PRIVATE).getInt(getString(R.string.storedBallSpeed), 50) / 100);
            lockType = intent.getIntExtra("lockType", 1);

        }
    }

    private void setAd() {
        String[] tip = {"Tilt your mobile to control Sensor Ball !", "Beware of Magnets !", "Look out for Angry Balls !", "Stay away from Holes !", "Keep an eye on Time as well !"};
        final LinearLayout layoutTip = (LinearLayout) findViewById(R.id.layoutTip);
        TextView textTip = (TextView) findViewById(R.id.textTip);
        textTip.setText("Tip : " + tip[new Random().nextInt(tip.length)]);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                firstAdReceived = true;
                layoutTip.setVisibility(View.GONE);
            }

            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (!firstAdReceived) {
                    // Keep code from part 1.
                    // Schedule an ad refresh.
                    refreshHandler.removeCallbacks(refreshRunnable);
                    refreshHandler.postDelayed(
                            refreshRunnable, REFRESH_RATE_IN_SECONDS * 1000);
                }
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdOpened() {
                firstAdReceived = true;
            }
        });

        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                ;
            }
        });

        // Request a new ad if one isn't already loaded
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            mInterstitialAd.loadAd(adRequest);
        }
    }

    private void setIntentData() {

		int screenHeight = getResources().getDisplayMetrics().heightPixels;
		TextView textLevel = (TextView) findViewById(R.id.textLevel);
        TextView textDifficulty = (TextView) findViewById(R.id.textDifficulty);
        textScore = (TextView) findViewById(R.id.textScore);
        textWord = (TextView) findViewById(R.id.textWord);
        textDifficulty.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9 * screenHeight / screenWidth);
        textDifficulty.setText(difficultyLevel);
        textLevel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8 * screenHeight / screenWidth);
        textLevel.setText("Level - " + levelNo);
        textScore.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10 * screenHeight / screenWidth);
        textScore.setText("Score - 0 / " + scoreNeeded);
        int txtSize = 20 * screenWidth / fixedParameter + (30 / word.length());
        textWord.setTextSize(TypedValue.COMPLEX_UNIT_SP, txtSize);
        textWord.setText(getDashWord());

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        animation.setDuration(2000);
        findViewById(R.id.textScore).startAnimation(animation);
		if (levelNo == 1)
            Toast.makeText(GameActivity.this, "Tilt your device to control the red ball !", Toast.LENGTH_LONG).show();
        else if (lockType == 1)
            Toast.makeText(GameActivity.this, "Complete the task in " + gameTime + " sec to unlock next level !", Toast.LENGTH_LONG).show();
    }

    private String getDashWord() {
        String wordWithDash = "";
        for (int i = 0; word != null && i < word.length(); i++)
            wordWithDash += i == word.length() - 1 ? "_" : "_ ";
        return wordWithDash;
    }

    private String getDashMixAlphabetString() {
        String wordFormed = "";
        for (int i = 0; i < word.length(); i++) {
            if (i < spellingCompleted.length())
                wordFormed += i == spellingCompleted.length() - 1 ? spellingCompleted.charAt(i) : spellingCompleted.charAt(i) + " ";
            else
                wordFormed += i == word.length() - 1 ? "_" : "_ ";
        }
        return wordFormed;
    }

    private void setBitmap() {
        redBallBitmap = SensorBallBitmap.getInstance(GameActivity.this, (int) sensorBallRadius, (int) sensorBallRadius);
        homeBitmap = HomeBitmap.getInstance(GameActivity.this);
        angryBallBitmap = AngryBallBitmap.getInstance(GameActivity.this);
        magnetBallBitmap = MagnetBallBitmap.getInstance(GameActivity.this);
    }

    private void initGameObjects() {

        if (gameViewObj != null && word != null) {
            sensorBallRadius = sensorBallRadius - Math.abs(sensorBallRadius - redBallBitmap.getWidth() / 2);
            gameViewObj.homeContainerBoxObj = new HomeContainerBox(
                    gameViewObj.roomWidth / 2 - 100 * gameViewObj.roomWidth / fixedParameter,
                    gameViewObj.roomHeight / 2 - 100 * gameViewObj.roomWidth / fixedParameter,
                    gameViewObj.roomWidth / 2 + 100 * gameViewObj.roomWidth / fixedParameter,
                    gameViewObj.roomHeight / 2 + 100 * gameViewObj.roomWidth / fixedParameter,
                    homeBitmap);
            gameViewObj.sensorBallObj = new SensorBall(GameActivity.this,
                    sensorBallVelocity,
                    gameViewObj.roomWidth / 2,
                    gameViewObj.roomHeight / 2,
                    redBallBitmap, sensorBallRadius, gameViewObj);
            gameViewObj.roomObj = new Room(0, 0, gameViewObj.roomWidth, gameViewObj.roomHeight);
            gameViewObj.collisionObj = new Collision();
            gameViewObj.pitObj = new Pit[noOfPits];
            gameViewObj.magnetBallObj = new MagnetBall[noOMagnetBalls];
            gameViewObj.angryBallObj = new AngryBall[noOfAngryBalls];
            gameViewObj.alphabetBallObj = new AlphabetBall[word.length()];
            roomRect = new Rect(0, 0, (int) gameViewObj.roomWidth, (int) gameViewObj.roomHeight);
        }
    }

    private void setPositions() {
        if (gameViewObj != null &&
                gameViewObj.collisionObj != null &&
                gameViewObj.magnetBallObj != null &&
                gameViewObj.angryBallObj != null &&
                gameViewObj.alphabetBallObj != null &&
                gameViewObj.pitObj != null) {

            float pitRadius = 75 * screenWidth / fixedParameter;
            float magentBallRadius = 45 * screenWidth / fixedParameter;
            float angryBallRadius = 45 * screenWidth / fixedParameter;
            float alphabetBallRadius = 45 * screenWidth / fixedParameter;
            int distanceFromWall = 25 * screenWidth / fixedParameter;
            float radius = pitRadius;
            long startTimeForCal = System.currentTimeMillis();
            Boolean isTimeTakenExceeded = false;
            for (int i = 0; i < (noOfPits + noOMagnetBalls + noOfAngryBalls + word.length()); i++) {
                Random rand = new Random();
                boolean ballsInCollision = true;
                float x = 0, y = 0;
                if (i >= noOfPits && i < noOfPits + noOMagnetBalls)
                    radius = magentBallRadius;
                else if (i >= noOfPits + noOMagnetBalls && i < noOfPits + noOMagnetBalls + noOfAngryBalls)
                    radius = angryBallRadius;
                else if (i >= noOfPits + noOMagnetBalls + noOfAngryBalls &&
                        i < noOfPits + noOMagnetBalls + noOfAngryBalls + word.length())
                    radius = alphabetBallRadius;
                float speed = (rand.nextInt(20) * 2 + 5) * screenWidth / fixedParameter;
                int angleInDegree = rand.nextInt(360);
                while (ballsInCollision) {
                    isTimeTakenExceeded = false;
                    if (((System.currentTimeMillis() - startTimeForCal) / 1000) >= 2) {
                        radius = pitRadius;
                        isTimeTakenExceeded = true;
                        i = 0;
                        startTimeForCal = System.currentTimeMillis();
                        break;
                    }
                    x = rand.nextInt((int) (gameViewObj.roomWidth - (2 * (radius + distanceFromWall)))) + radius + distanceFromWall;
                    y = rand.nextInt((int) (gameViewObj.roomHeight - (2 * (radius + distanceFromWall)))) + radius + distanceFromWall;
                    ballsInCollision = false;
                    for (int j = i - 1; j >= 0; j--) {
                        if (j < noOfPits) {
                            if (gameViewObj.collisionObj.isCollisionPossible(x, y, radius, gameViewObj.pitObj[j].positionX, gameViewObj.pitObj[j].positionY, gameViewObj.pitObj[j].radius) ||
                                    !gameViewObj.collisionObj.isPositionProper(x, y, gameViewObj.pitObj[j].positionX, gameViewObj.pitObj[j].positionY, screenWidth / 4))
                                ballsInCollision = true;
                        } else if (j < noOfPits + noOMagnetBalls) {
                            if (gameViewObj.collisionObj.isCollisionPossible(x, y, radius,
                                    gameViewObj.magnetBallObj[j - noOfPits].positionX,
                                    gameViewObj.magnetBallObj[j - noOfPits].positionY,
                                    gameViewObj.magnetBallObj[j - noOfPits].radius) ||
                                    !gameViewObj.collisionObj.isPositionProper(x, y,
                                            gameViewObj.magnetBallObj[j - noOfPits].positionX,
                                            gameViewObj.magnetBallObj[j - noOfPits].positionY,
                                            screenWidth / 16))
                                ballsInCollision = true;
                        } else if (j < noOfPits + noOMagnetBalls + noOfAngryBalls) {
                            if (gameViewObj.collisionObj.isCollisionPossible(x, y, radius,
                                    gameViewObj.angryBallObj[j - noOfPits - noOMagnetBalls].positionX,
                                    gameViewObj.angryBallObj[j - noOfPits - noOMagnetBalls].positionY,
                                    gameViewObj.angryBallObj[j - noOfPits - noOMagnetBalls].radius) ||
                                    !gameViewObj.collisionObj.isPositionProper(x, y,
                                            gameViewObj.angryBallObj[j - noOfPits - noOMagnetBalls].positionX,
                                            gameViewObj.angryBallObj[j - noOfPits - noOMagnetBalls].positionY,
                                            screenWidth / 16))
                                ballsInCollision = true;
                        } else if (j < noOfPits + noOMagnetBalls + noOfAngryBalls + word.length()) {
                            if (gameViewObj.collisionObj.isCollisionPossible(x, y, radius,
                                    gameViewObj.alphabetBallObj[j - noOfPits - noOMagnetBalls - noOfAngryBalls].positionX,
                                    gameViewObj.alphabetBallObj[j - noOfPits - noOMagnetBalls - noOfAngryBalls].positionY,
                                    gameViewObj.alphabetBallObj[j - noOfPits - noOMagnetBalls - noOfAngryBalls].radius) ||
                                    !gameViewObj.collisionObj.isPositionProper(x, y,
                                            gameViewObj.alphabetBallObj[j - noOfPits - noOMagnetBalls - noOfAngryBalls].positionX,
                                            gameViewObj.alphabetBallObj[j - noOfPits - noOMagnetBalls - noOfAngryBalls].positionY,
                                            screenWidth / 16))
                                ballsInCollision = true;
                        }
                    }
                    if ((x + radius) >= gameViewObj.roomWidth / 2 - 100 * gameViewObj.roomWidth / fixedParameter
                            && (x - radius) <= gameViewObj.roomWidth / 2 + 100 * gameViewObj.roomWidth / fixedParameter
                            && (y + radius) >= gameViewObj.roomHeight / 2 - 100 * gameViewObj.roomWidth / fixedParameter
                            && (y - radius) <= gameViewObj.roomHeight / 2 + 100 * gameViewObj.roomWidth / fixedParameter)
                        ballsInCollision = true;


                }
                if (!isTimeTakenExceeded) {
                    if (i < noOfPits)
                        gameViewObj.pitObj[i] = new Pit(x, y, radius);
                    else if (i < noOfPits + noOMagnetBalls) {
                        radius = radius - Math.abs(radius - magnetBallBitmap.getWidth() / 2);
                        gameViewObj.magnetBallObj[i - noOfPits] = new MagnetBall(x, y, radius, speed, angleInDegree, magnetBallBitmap);
                    } else if (i < noOfPits + noOMagnetBalls + noOfAngryBalls) {
                        radius = radius - Math.abs(radius - angryBallBitmap.getWidth() / 2);
                        gameViewObj.angryBallObj[i - noOfPits - noOMagnetBalls] = new AngryBall(x, y, radius, speed, angleInDegree, angryBallBitmap);
                    } else if (i < noOfPits + noOMagnetBalls + noOfAngryBalls + word.length())
                        gameViewObj.alphabetBallObj[i - noOfPits - noOMagnetBalls - noOfAngryBalls] = new AlphabetBall(x, y, radius, speed, angleInDegree, word.charAt(i - noOfPits - noOMagnetBalls - noOfAngryBalls), screenWidth);
                }
            }
        }
    }

    private void checkCollision() {
        if (gameViewObj != null &&
                gameViewObj.collisionObj != null
                && !gameViewObj.isGameOver
                && word != null) {

            for (int i = 0; i < word.length(); i++)
                for (int j = 0; j < word.length(); j++)
                    if (i < j)
                        gameViewObj.collisionObj.intersect(gameViewObj.alphabetBallObj[i], gameViewObj.alphabetBallObj[j]);

            for (int i = 0; i < noOMagnetBalls; i++)
                for (int j = 0; j < noOMagnetBalls; j++)
                    if (i < j)
                        gameViewObj.collisionObj.intersect(gameViewObj.magnetBallObj[i], gameViewObj.magnetBallObj[j]);

            for (int i = 0; i < noOfAngryBalls; i++)
                for (int j = 0; j < noOfAngryBalls; j++)
                    if (i < j)
                        gameViewObj.collisionObj.intersect(gameViewObj.angryBallObj[i], gameViewObj.angryBallObj[j]);

            for (int i = 0; i < noOfAngryBalls; i++)
                for (int j = 0; j < noOMagnetBalls; j++)
                    gameViewObj.collisionObj.intersect(gameViewObj.angryBallObj[i], gameViewObj.magnetBallObj[j]);

            for (int i = 0; i < noOfAngryBalls; i++)
                for (int j = 0; j < word.length(); j++)
                    gameViewObj.collisionObj.intersect(gameViewObj.angryBallObj[i], gameViewObj.alphabetBallObj[j]);

            for (int i = 0; i < word.length(); i++)
                for (int j = 0; j < noOMagnetBalls; j++)
                    gameViewObj.collisionObj.intersect(gameViewObj.alphabetBallObj[i], gameViewObj.magnetBallObj[j]);

            for (int i = 0; i < noOfPits; i++)
                for (int j = 0; j < noOfAngryBalls; j++)
                    gameViewObj.collisionObj.intersect(gameViewObj.pitObj[i], gameViewObj.angryBallObj[j]);

            for (int i = 0; i < noOfPits; i++)
                for (int j = 0; j < noOMagnetBalls; j++)
                    gameViewObj.collisionObj.intersect(gameViewObj.pitObj[i], gameViewObj.magnetBallObj[j]);

            for (int i = 0; i < noOfPits; i++)
                for (int j = 0; j < word.length(); j++)
                    gameViewObj.collisionObj.intersect(gameViewObj.pitObj[i], gameViewObj.alphabetBallObj[j]);

            for (int j = 0; j < noOfAngryBalls; j++)
                gameViewObj.collisionObj.intersect(gameViewObj.roomObj, gameViewObj.angryBallObj[j]);

            for (int j = 0; j < noOMagnetBalls; j++)
                gameViewObj.collisionObj.intersect(gameViewObj.roomObj, gameViewObj.magnetBallObj[j]);

            for (int j = 0; j < word.length(); j++)
                gameViewObj.collisionObj.intersect(gameViewObj.roomObj, gameViewObj.alphabetBallObj[j]);

            if (!gameViewObj.isSensorBallSafe) {

                collisionWithAlphabetBall();
                collisionWithMagnetBall();
                collisionWithAngryBall();
                collisionWithPit();
            }
        }
    }

    private void updateExplosion() {
        if (gameViewObj.explosion != null && gameViewObj.explosion.isAlive()) {
            gameViewObj.explosion.update(roomRect);
        }
    }

    private void collisionWithMagnetBall() {
        if (gameViewObj != null) {
            for (int j = 0; j < noOMagnetBalls && !gameViewObj.isGameOver && gameViewObj.magnetBallObj[j] != null; j++) {
                if (gameViewObj.collisionObj != null && gameViewObj.sensorBallObj != null && gameViewObj.collisionObj.intersect(gameViewObj.sensorBallObj, gameViewObj.magnetBallObj[j])) {
                    playFailureMusic();
                    gameViewObj.sensorBallObj.positionX = Float.MAX_VALUE;
                    gameViewObj.sensorBallObj.positionY = Float.MAX_VALUE;
                    gameViewObj.sensorBallObj.radius = 0;
                    gameViewObj.sensorBallObj.stopSensor();
                    gameViewObj.isGameOver = true;
                    if (gameViewObj.explosion == null || gameViewObj.explosion.getState() == Explosion.STATE_DEAD || gameViewObj.explosion.getState() == Explosion.STATE_ALIVE)
                        gameViewObj.explosion = new Explosion(gameViewObj.EXPLOSION_SIZE, (int) gameViewObj.magnetBallObj[j].positionX, (int) gameViewObj.magnetBallObj[j].positionY);
                    gameOverReason = 2;
                }
            }
        }
    }

    private void collisionWithAngryBall() {
        if (gameViewObj != null) {
            for (int j = 0; j < noOfAngryBalls && !gameViewObj.isGameOver && gameViewObj.angryBallObj[j] != null; j++) {
                if (gameViewObj.collisionObj != null && gameViewObj.sensorBallObj != null && gameViewObj.collisionObj.intersect(gameViewObj.sensorBallObj, gameViewObj.angryBallObj[j])) {
                    playFailureMusic();
                    gameViewObj.sensorBallObj.positionX = Float.MAX_VALUE;
                    gameViewObj.sensorBallObj.positionY = Float.MAX_VALUE;
                    gameViewObj.sensorBallObj.radius = 0;
                    gameViewObj.sensorBallObj.stopSensor();
                    gameViewObj.isGameOver = true;
                    if (gameViewObj.explosion == null || gameViewObj.explosion.getState() == Explosion.STATE_DEAD || gameViewObj.explosion.getState() == Explosion.STATE_ALIVE)
                        gameViewObj.explosion = new Explosion(gameViewObj.EXPLOSION_SIZE, (int) gameViewObj.angryBallObj[j].positionX, (int) gameViewObj.angryBallObj[j].positionY);
                    gameOverReason = 3;
                }
            }
        }
    }

    private void collisionWithAlphabetBall() {
        if (gameViewObj != null && word != null) {
            for (int j = 0; j < word.length() && !gameViewObj.isGameOver && gameViewObj.alphabetBallObj[j] != null; j++) {
                if (gameViewObj.collisionObj != null && gameViewObj.sensorBallObj != null &&
                        gameViewObj.collisionObj.intersect(gameViewObj.sensorBallObj, gameViewObj.alphabetBallObj[j]) &&
                        !gameViewObj.alphabetBallObj[j].isSucked) {
                    spellingCompleted += gameViewObj.alphabetBallObj[j].spellingLetter;
                    gameViewObj.alphabetBallObj[j].isSucked = true;
                    if (word.equals(spellingCompleted)) {
                        gameViewObj.sensorBallObj.stopSensor();
                        gameViewObj.isGameOver = true;
                        gameOverReason = 6;
                    }
                    if (spellingCompleted != null && !word.substring(0, spellingCompleted.length()).equals(spellingCompleted)) {
                        playFailureMusic();
                        gameViewObj.sensorBallObj.positionX = Float.MAX_VALUE;
                        gameViewObj.sensorBallObj.positionY = Float.MAX_VALUE;
                        gameViewObj.sensorBallObj.radius = 0;
                        gameViewObj.sensorBallObj.stopSensor();
                        gameViewObj.isGameOver = true;
                        gameOverReason = 5;
                    } else {
                        playEngulfMusic();
                        gameViewObj.alphabetBallObj[j].radius = 0;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (textWord != null && textScore != null && spellingCompleted != null && word != null) {
                                    textWord.setText(getDashMixAlphabetString());
                                    textScore.setText("Score - " + (scoreNeeded / word.length()) * spellingCompleted.length() + " / " + scoreNeeded);
                                }

                            }
                        });
                    }
                    if (gameViewObj.explosion == null || gameViewObj.explosion.getState() == Explosion.STATE_DEAD || gameViewObj.explosion.getState() == Explosion.STATE_ALIVE)
                        gameViewObj.explosion = new Explosion(gameViewObj.EXPLOSION_SIZE, (int) gameViewObj.alphabetBallObj[j].positionX, (int) gameViewObj.alphabetBallObj[j].positionY);

                }
            }
        }
    }

    private void collisionWithPit() {
        if (gameViewObj != null) {
            for (int i = 0; i < noOfPits && !gameViewObj.isGameOver && gameViewObj.pitObj[i] != null; i++) {
                if (gameViewObj.collisionObj != null && gameViewObj.sensorBallObj != null &&
                        gameViewObj.collisionObj.intersect(gameViewObj.sensorBallObj, gameViewObj.pitObj[i])) {
                    playFailureMusic();
                    gameViewObj.sensorBallObj.positionX = Float.MAX_VALUE;
                    gameViewObj.sensorBallObj.positionY = Float.MAX_VALUE;
                    gameViewObj.sensorBallObj.radius = 0;
                    gameViewObj.sensorBallObj.stopSensor();
                    gameViewObj.isGameOver = true;
                    if (gameViewObj.explosion == null || gameViewObj.explosion.getState() == Explosion.STATE_DEAD || gameViewObj.explosion.getState() == Explosion.STATE_ALIVE)
                        gameViewObj.explosion = new Explosion(gameViewObj.EXPLOSION_SIZE, (int) gameViewObj.pitObj[i].positionX, (int) gameViewObj.pitObj[i].positionY);
                    gameOverReason = 4;
                }
            }
        }
    }

    private void deInit() {

        if (gameViewObj != null) {
            gameViewObj.isGameOver = true;
            if (gameViewObj.sensorBallObj != null) {
                gameViewObj.sensorBallObj.stopSensor();
            }
        }

        if (mpSecret != null) {
            mpSecret.stop();
            mpSecret.release();
            mpSecret = null;
        }

        if (mpFailure != null) {
            mpFailure.stop();
            mpFailure.release();
            mpFailure = null;
        }

        if (mpEngulf != null) {
            mpEngulf.stop();
            mpEngulf.release();
            mpEngulf = null;
        }
    }

    private void initMusic() {
        mpSecret = MediaPlayer.create(GameActivity.this, R.raw.secret);
        mpEngulf = MediaPlayer.create(GameActivity.this, R.raw.gotitem);
        mpFailure = MediaPlayer.create(GameActivity.this, R.raw.flight);
        mpSecret.setLooping(true);
        mpSecret.start();

    }

    private void playFailureMusic() {

        if (mpSecret != null) {
            mpSecret.stop();
            mpSecret.release();
            mpSecret = null;
        }

        if (mpEngulf != null) {
            mpEngulf.stop();
            mpEngulf.release();
            mpEngulf = null;
        }

        mpFailure.start();
    }

    private void playEngulfMusic() {
        if (gameViewObj != null && gameViewObj.isGameOver) {
            if (mpSecret != null) {
                mpSecret.stop();
                mpSecret.release();
                mpSecret = null;
            }
        }
        if (mpEngulf != null) {
            mpEngulf.stop();
            mpEngulf.release();
            mpEngulf = null;
        }
        mpEngulf = MediaPlayer.create(GameActivity.this, R.raw.gotitem);
        mpEngulf.start();
    }

    private void playClickMusic() {
        if (mpClick != null) {
            mpClick.stop();
            mpClick.release();
            mpClick = null;
        }
        mpClick = MediaPlayer.create(this, R.raw.buttonclick);
        mpClick.start();
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.btnHome:
                    playClickMusic();
                    if (gameViewObj != null && !gameViewObj.isGameOver) {
                        gameViewObj.isGameOver = true;
                        event = HOME_PRESSED;
                    }
                    break;
                case R.id.btnHint:
                    playClickMusic();
                    if (gameViewObj != null && gameViewObj.isSensorBallSafe) {
                        final Dialog dialog = new Dialog(this);

                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawableResource(R.color.color_white);
                        dialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialog.setContentView(R.layout.level_content_item);
                        dialog.getWindow().setLayout(getResources().getDisplayMetrics().widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);

                        ((TextView) dialog.findViewById(R.id.textDifficulty)).setText(difficultyLevel);
                        ((TextView) dialog.findViewById(R.id.textLevel)).setText("Level - " + levelNo);
                        ((TextView) dialog.findViewById(R.id.textHint)).setText("Find a relevant wordo.\n\n" + gloss);
                        ((TextView) dialog.findViewById(R.id.textScore)).setText("Score Required : \n" + scoreNeeded);
                        ((TextView) dialog.findViewById(R.id.textTime)).setText("Time : \n" + gameTime + " sec");

                        dialog.findViewById(R.id.btnPlay).setVisibility(View.GONE);
                        dialog.show();
                    } else if (gameViewObj != null)
                        Toast.makeText(GameActivity.this, "Hint cannot be displayed during the game.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnBack:
                    playClickMusic();
                    if (gameViewObj != null && !gameViewObj.isGameOver) {
                        gameViewObj.isGameOver = true;
                        event = BACK_BUTTON_PRESSED;
                    }
                    break;
            }
        }
    }

    private class RefreshRunnable implements Runnable {
        @Override
        public void run() {
            // Load an ad with an ad request.
            if (mAdView == null) {
                mAdView = (AdView) findViewById(R.id.adView);
            }

            mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            mAdView.loadAd(adRequest);
        }
    }
}
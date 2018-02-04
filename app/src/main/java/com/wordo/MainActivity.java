package com.wordo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.wordo.levels.LevelActivity;

/**
 * Created by Arihant on 17-03-2016.
 */
public class MainActivity extends BaseGameActivity implements View.OnClickListener {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private MediaPlayer mpClick;
    private Boolean isLeaderboardClicked = false;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (getGameHelper() != null)
            getGameHelper().setMaxAutoSignInAttempts(0);

        super.onCreate(savedInstanceState);

        if (getGameHelper() != null)
            getGameHelper().setMaxAutoSignInAttempts(0);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        final String sharedPreferenceFileName = "wordo_data";
        sharedPref = getSharedPreferences(sharedPreferenceFileName, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        isLeaderboardClicked = false;

        setScore();

        setButtonListener();

        if (getIntent().getStringExtra("classFrom") != null && getIntent().getStringExtra("classFrom").equals(SplashActivity.class.toString()))
            Toast.makeText(this, "Polish Your Vocabulary. Click Play.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSelectDifficulty:
                playSound();
                onSelectDifficulty();
                break;
            case R.id.btnPlay:
                playSound();
                onPlay();
                break;
            case R.id.btnSettings:
                playSound();
                onSelectSettings();
                break;
            case R.id.btnShare:
                playSound();
                onSelectShare();
                break;
            case R.id.btnLeaderboard:
                playSound();
                onSelectLeaderboard();
                break;
        }
    }

    private void playSound() {
        if (mpClick != null) {
            mpClick.stop();
            mpClick.release();
            mpClick = null;
        }
        mpClick = MediaPlayer.create(this, R.raw.buttonclick);
        mpClick.start();
    }

    private void setButtonListener() {
        findViewById(R.id.btnPlay).setOnClickListener(this);
        findViewById(R.id.btnSelectDifficulty).setOnClickListener(this);
        findViewById(R.id.btnSettings).setOnClickListener(this);
        findViewById(R.id.btnShare).setOnClickListener(this);
        findViewById(R.id.btnLeaderboard).setOnClickListener(this);
    }

    private void setScore() {
        if (sharedPref != null)
            ((Button) findViewById(R.id.btnScore)).setText(sharedPref.getInt(getString(R.string.storedMyWordoScore), 0) + "\n" +
                    "My Wordo Score");
    }

    private void onSelectDifficulty() {
        if (sharedPref != null && editor != null) {

            Dialog dialogSelectDifficulty = new Dialog(this);
            dialogSelectDifficulty.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogSelectDifficulty.getWindow().setBackgroundDrawableResource(R.color.color_white);
            dialogSelectDifficulty.getWindow().getAttributes().gravity = Gravity.BOTTOM;
            dialogSelectDifficulty.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialogSelectDifficulty.setContentView(R.layout.select_difficulty_item);
            dialogSelectDifficulty.getWindow().setLayout(getResources().getDisplayMetrics().widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);

            RadioGroup radioGroup = (RadioGroup) dialogSelectDifficulty.findViewById(R.id.radioGroupSelectDifficulty);
            ((RadioButton) radioGroup.getChildAt(sharedPref.getInt(getString(R.string.storedSelectDifficulty), 0))).setChecked(true);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int childCount = radioGroup.getChildCount();
                    for (int j = 0; j < childCount; j++) {
                        if ((radioGroup.getChildAt(j)).getId() == i) {
                            editor.putInt(getString(R.string.storedSelectDifficulty), j);
                            editor.commit();
                        }
                    }
                }
            });

            dialogSelectDifficulty.show();
        }
    }

    private void onPlay() {
        startActivity(new Intent(MainActivity.this, LevelActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void onSelectShare() {
        String shareBody = "https://play.google.com/store/apps/details?id=com.wordo";

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "A complete package to polish vocabulary for GRE, GMAT, SAT, TOEFL Exams - DOWNLOAD WORDO APP NOW !");

        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void onSelectLeaderboard() {
        isLeaderboardClicked = true;
        if (!isSignedIn()) {
            beginUserInitiatedSignIn();
        } else {
            Games.Leaderboards.submitScore(getApiClient(),
                    getString(R.string.leaderboard_high_scores),
                    sharedPref.getInt(getString(R.string.storedMyWordoScore), 0));
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
                    getApiClient(), getString(R.string.leaderboard_high_scores)), 2);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private void onSelectSettings() {
        if (sharedPref != null && editor != null) {

            Dialog dialogSettings = new Dialog(this);
            dialogSettings.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogSettings.getWindow().setBackgroundDrawableResource(R.color.color_white);
            dialogSettings.getWindow().getAttributes().gravity = Gravity.BOTTOM;
            dialogSettings.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialogSettings.setContentView(R.layout.settings_item);
            dialogSettings.getWindow().setLayout(getResources().getDisplayMetrics().widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);

            final SeekBar seekBarBallRadius = (SeekBar) dialogSettings.findViewById(R.id.seekBarBallRadius);
            seekBarBallRadius.setProgress(sharedPref.getInt(getString(R.string.storedBallRadius), 50));
            seekBarBallRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    editor.putInt(getString(R.string.storedBallRadius), progress);
                    editor.commit();
                    seekBarBallRadius.setProgress(progress);
                }

                public void onStartTrackingTouch(SeekBar arg0) {
                    // TODO Auto-generated method stub

                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub

                }
            });

            final SeekBar seekBarBallSpeed = (SeekBar) dialogSettings.findViewById(R.id.seekBarBallSpeed);
            seekBarBallSpeed.setProgress(sharedPref.getInt(getString(R.string.storedBallSpeed), 50));
            seekBarBallSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    editor.putInt(getString(R.string.storedBallSpeed), progress);
                    editor.commit();
                    seekBarBallSpeed.setProgress(progress);
                }

                public void onStartTrackingTouch(SeekBar arg0) {
                    // TODO Auto-generated method stub

                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub

                }
            });

            dialogSettings.show();
        }
    }

    @Override
    public void onSignInFailed() {
        if (isLeaderboardClicked) {
            isLeaderboardClicked = false;
            new AlertDialog.Builder(this)
                    .setTitle("Android Play Store Connection Failure !")
                    .setMessage("It was probably due to a loss of connection with the service. Internet Connection Issue.")
                    .setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            beginUserInitiatedSignIn();
                        }
                    })
                    .setCancelable(true)
                    .setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                            finish();
                            return true;
                        }
                    })
                    .create();
        }
    }

    @Override
    public void onSignInSucceeded() {
        if (isLeaderboardClicked && getApiClient().isConnected()) {
            isLeaderboardClicked = false;
            Games.Leaderboards.submitScore(getApiClient(),
                    getString(R.string.leaderboard_high_scores),
                    sharedPref.getInt(getString(R.string.storedMyWordoScore), 0));
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
                    getApiClient(), getString(R.string.leaderboard_high_scores)), 2);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}

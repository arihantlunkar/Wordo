package com.wordo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by Arihant on 16-03-2016.
 */
public class SplashActivity extends Activity {


    private Boolean isSplashThreadRunning = true;
    private Boolean isMainActivityCalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Thread splashThread = new Thread() {
            int wait = 0;
            int splashTimeOut = 2000;

            @Override
            public void run() {
                try {
                    super.run();
                    while (wait < splashTimeOut && isSplashThreadRunning) {
                        sleep(100);
                        wait += 100;
                    }
                } catch (Exception e) {
                    Log.d("Message", "Exception", e.getCause());
                } finally {
                    if (isSplashThreadRunning) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isMainActivityCalled = true;
                                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                                i.putExtra("classFrom", SplashActivity.class.toString());
                                startActivity(i);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        });
                    }
                }
            }
        };
        splashThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        isSplashThreadRunning = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isMainActivityCalled && !isSplashThreadRunning) {
            isSplashThreadRunning = true;
            finish();
            startActivity(getIntent());
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
    }
}


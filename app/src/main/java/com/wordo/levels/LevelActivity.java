package com.wordo.levels;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Toast;

import com.wordo.DatabaseAccess;
import com.wordo.MainActivity;
import com.wordo.R;
import com.wordo.game.DatabaseDataUnit;
import com.wordo.game.GameAllocatableUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Arihant on 23-06-2016.
 */
public class LevelActivity extends ListActivity {

    private static final int PAGESIZE = 5;
    private final String[] difficultyLevels = {"Amateur", "Semi Pro ", "Professional ", "World Class", "Legendary"};
    private List<GameAllocatableUnit> listGameAllocatableUnit;
    private int NONE = -1, BACK_PRESSED = 1, GAME_DESTROY = 3;
    private int event = NONE;
    private DatabaseAccess databaseAccess;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private int storedSelectDifficulty = 0, noOfSolvedLevels = 0;
    private View footerView;

    private boolean loading = false;

    private int LOCKED = 0, UNLOCKED_UNSOLVED = 1, UNLOCKED_SOLVED = 2;

    private String toastText;

    @Override
    public void onBackPressed() {
        event = BACK_PRESSED;
        startActivity(new Intent(LevelActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        event = GAME_DESTROY;

        if (databaseAccess != null)
            databaseAccess.close();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_level);

        footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer, null, false);
        (new LoadData()).execute("");

    }


    private void openDatabase() {
        databaseAccess = DatabaseAccess.getInstance(LevelActivity.this);
        databaseAccess.open();
    }

    private void init() {
        sharedPref = getSharedPreferences("wordo_data", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        listGameAllocatableUnit = new ArrayList<>();
        storedSelectDifficulty = sharedPref.getInt(getString(R.string.storedSelectDifficulty), 0);
    }

    private void updatePendingQuery() {
        if (sharedPref != null && databaseAccess != null && !sharedPref.getString(getString(R.string.storedQuery), "").equals("")) {
            databaseAccess.updatePendingQuery(sharedPref.getString(getString(R.string.storedQuery), ""));
            editor.putString(getString(R.string.storedQuery), "");
            editor.commit();
        }

        if (databaseAccess != null)
            noOfSolvedLevels = databaseAccess.getNumberOfSolvedLevels(storedSelectDifficulty);
    }

    private void getLevels() {
        if (databaseAccess != null) {
            if (sharedPref != null) {
                if (sharedPref.getString(difficultyLevels[storedSelectDifficulty].toUpperCase() + " Level", "new").equals("new") && editor != null) {
                    List<DatabaseDataUnit> databaseDataUnitList = databaseAccess.returnDatabaseLevelData(storedSelectDifficulty, 0, "LIMIT 1", "ORDER BY Random()");
                    if (databaseDataUnitList.isEmpty()) {
                        toastText = "Congratulations ! You have completed all levels. Switch to next difficulty.";
                    } else {
                        if (sharedPref.getInt(difficultyLevels[storedSelectDifficulty].toUpperCase() + " Score", -1) != noOfSolvedLevels && editor != null) {
                            toastText = "A level is unlocked. Complete the task to unlock more levels.";
                            editor.putInt(difficultyLevels[storedSelectDifficulty].toUpperCase() + " Score", noOfSolvedLevels);
                            editor.commit();
                        }
                        setGameAllocatableUnit(databaseDataUnitList, UNLOCKED_UNSOLVED, true);
                        editor.putString(difficultyLevels[storedSelectDifficulty].toUpperCase() + " Level", "old");
                        editor.commit();
                    }
                } else if (sharedPref.getString(difficultyLevels[storedSelectDifficulty].toUpperCase() + " Level", "new").equals("old") && listGameAllocatableUnit != null) {
                    GameAllocatableUnit gameAllocatableUnitObj = new GameAllocatableUnit();
                    gameAllocatableUnitObj.setWord(sharedPref.getString(difficultyLevels[storedSelectDifficulty].toUpperCase() + " Word", "new"));
                    gameAllocatableUnitObj.setGloss(sharedPref.getString(difficultyLevels[storedSelectDifficulty].toUpperCase() + " Gloss", "new"));
                    gameAllocatableUnitObj.setWordID(sharedPref.getString(difficultyLevels[storedSelectDifficulty].toUpperCase() + " WordID", "new"));
                    gameAllocatableUnitObj.setDifficultyLevel(difficultyLevels[storedSelectDifficulty].toUpperCase());
                    gameAllocatableUnitObj.setLevelNo(sharedPref.getInt(difficultyLevels[storedSelectDifficulty].toUpperCase() + " LevelNo", 1));
                    gameAllocatableUnitObj.setGameTime(sharedPref.getInt(difficultyLevels[storedSelectDifficulty].toUpperCase() + " GameTime", 1));
                    gameAllocatableUnitObj.setNoOfPits(sharedPref.getInt(difficultyLevels[storedSelectDifficulty].toUpperCase() + " NoOfPits", 1));
                    gameAllocatableUnitObj.setNoOfAngryBalls(sharedPref.getInt(difficultyLevels[storedSelectDifficulty].toUpperCase() + " NoOfAngryBalls", 1));
                    gameAllocatableUnitObj.setNoOfMagnetBalls(sharedPref.getInt(difficultyLevels[storedSelectDifficulty].toUpperCase() + " NoOfMagnetBalls", 1));
                    gameAllocatableUnitObj.setScoreNeeded(sharedPref.getInt(difficultyLevels[storedSelectDifficulty].toUpperCase() + " ScoreNeeded", 1));
                    gameAllocatableUnitObj.setLockType(UNLOCKED_UNSOLVED);
                    listGameAllocatableUnit.add(gameAllocatableUnitObj);
                }
            }
            if (noOfSolvedLevels >= 6) {
                List<DatabaseDataUnit> databaseDataUnitList = databaseAccess.returnDatabaseLevelData(storedSelectDifficulty, 1, "LIMIT 6", "ORDER BY no_of_attempts DESC");
                setGameAllocatableUnit(databaseDataUnitList, UNLOCKED_SOLVED, false);
            } else {
                setGameAllocatableUnitFool(6 - noOfSolvedLevels - 1);
                List<DatabaseDataUnit> databaseDataUnitList = databaseAccess.returnDatabaseLevelData(storedSelectDifficulty, 1, "LIMIT " + noOfSolvedLevels, "ORDER BY no_of_attempts DESC");
                setGameAllocatableUnit(databaseDataUnitList, UNLOCKED_SOLVED, false);
            }
        }
    }

    private void setGameAllocatableUnit(List<DatabaseDataUnit> databaseDataUnitList, int lockType, Boolean commitRequired) {
        if (databaseDataUnitList != null && listGameAllocatableUnit != null && editor != null) {
            int scoreNeeded;
            int gameTime;
            int noOfPits;
            int noOfAngryBalls;
            int noOfMagnetBalls;
            int level;

            for (int i = 0; i < databaseDataUnitList.size(); i++) {
                switch (storedSelectDifficulty) {
                    default:
                        noOfPits = new Random().nextInt(3) + 3;
                        noOfAngryBalls = new Random().nextInt(3) + 3;
                        noOfMagnetBalls = new Random().nextInt(3) + 3;
                        gameTime = 30;
                        break;
                    case 1:
                        noOfPits = new Random().nextInt(3) + 3;
                        noOfAngryBalls = new Random().nextInt(3) + 3;
                        noOfMagnetBalls = new Random().nextInt(3) + 3;
                        gameTime = 60;
                        break;
                    case 2:
                        noOfPits = new Random().nextInt(3) + 2;
                        noOfAngryBalls = new Random().nextInt(3) + 2;
                        noOfMagnetBalls = new Random().nextInt(3) + 2;
                        gameTime = 90;
                        break;
                    case 3:
                        noOfPits = new Random().nextInt(3) + 2;
                        noOfAngryBalls = new Random().nextInt(3) + 2;
                        noOfMagnetBalls = new Random().nextInt(3) + 2;
                        gameTime = 120;
                        break;
                    case 4:
                        noOfPits = new Random().nextInt(3) + 2;
                        noOfAngryBalls = new Random().nextInt(3) + 2;
                        noOfMagnetBalls = new Random().nextInt(3) + 2;
                        gameTime = 180;
                        break;
                }
                String gloss = databaseDataUnitList.get(i).getGloss().substring(0, 1).toUpperCase() + databaseDataUnitList.get(i).getGloss().substring(1) + ".";
                level = databaseDataUnitList.get(i).getLevelNo() == 0 ? noOfSolvedLevels + 1 : databaseDataUnitList.get(i).getLevelNo();
                scoreNeeded = (noOfPits * 10 + noOfAngryBalls * 10 + noOfMagnetBalls * 10 + gameTime * 10 + storedSelectDifficulty * 10) * 10 * level;
                GameAllocatableUnit gameAllocatableUnitObj = new GameAllocatableUnit();
                gameAllocatableUnitObj.setWord(databaseDataUnitList.get(i).getWord().toUpperCase());
                gameAllocatableUnitObj.setGloss(gloss);
                gameAllocatableUnitObj.setWordID(databaseDataUnitList.get(i).getWordID());
                gameAllocatableUnitObj.setDifficultyLevel(difficultyLevels[storedSelectDifficulty].toUpperCase());
                gameAllocatableUnitObj.setLevelNo(level);
                gameAllocatableUnitObj.setGameTime(gameTime);
                gameAllocatableUnitObj.setNoOfPits(noOfPits);
                gameAllocatableUnitObj.setNoOfAngryBalls(noOfAngryBalls);
                gameAllocatableUnitObj.setNoOfMagnetBalls(noOfMagnetBalls);
                gameAllocatableUnitObj.setScoreNeeded(scoreNeeded);
                gameAllocatableUnitObj.setLockType(lockType);
                listGameAllocatableUnit.add(gameAllocatableUnitObj);
                if (commitRequired) {
                    editor.putString(difficultyLevels[storedSelectDifficulty].toUpperCase() + " Word", databaseDataUnitList.get(i).getWord().toUpperCase());
                    editor.putString(difficultyLevels[storedSelectDifficulty].toUpperCase() + " Gloss", gloss);
                    editor.putString(difficultyLevels[storedSelectDifficulty].toUpperCase() + " WordID", databaseDataUnitList.get(i).getWordID());
                    editor.putInt(difficultyLevels[storedSelectDifficulty].toUpperCase() + " LevelNo", level);
                    editor.putInt(difficultyLevels[storedSelectDifficulty].toUpperCase() + " GameTime", gameTime);
                    editor.putInt(difficultyLevels[storedSelectDifficulty].toUpperCase() + " NoOfPits", noOfPits);
                    editor.putInt(difficultyLevels[storedSelectDifficulty].toUpperCase() + " NoOfAngryBalls", noOfAngryBalls);
                    editor.putInt(difficultyLevels[storedSelectDifficulty].toUpperCase() + " NoOfMagnetBalls", noOfMagnetBalls);
                    editor.putInt(difficultyLevels[storedSelectDifficulty].toUpperCase() + " ScoreNeeded", scoreNeeded);
                    editor.commit();
                }
            }
        }
    }

    private void setGameAllocatableUnitFool(int times) {
        if (listGameAllocatableUnit != null) {
            for (int i = 0; i < times; i++) {
                GameAllocatableUnit gameAllocatableUnitObj = new GameAllocatableUnit();
                gameAllocatableUnitObj.setLevelNo(noOfSolvedLevels + 2 + i);
                gameAllocatableUnitObj.setLockType(LOCKED);
                listGameAllocatableUnit.add(gameAllocatableUnitObj);
            }
        }
    }

    protected boolean load(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean lastItem = firstVisibleItem + visibleItemCount == totalItemCount && getListView().getChildAt(visibleItemCount - 1) != null && getListView().getChildAt(visibleItemCount - 1).getBottom() <= getListView().getHeight();
        return lastItem && !loading && getListAdapter().getCount() < (noOfSolvedLevels >= 6 ? noOfSolvedLevels + 1 : 6);
    }

    protected class LoadPage extends AsyncTask<String, Void, String> {
        private CustomListAdapter customListAdapter;

        LoadPage(CustomListAdapter customListAdapter) {
            this.customListAdapter = customListAdapter;
        }

        @Override
        protected String doInBackground(String... arg0) {
            if (databaseAccess != null) {
                List<DatabaseDataUnit> databaseDataUnitList = databaseAccess.returnDatabaseLevelData(storedSelectDifficulty, 1, "LIMIT " + (getListAdapter().getCount() - 1) + "," + PAGESIZE, "ORDER BY no_of_attempts DESC");
                listGameAllocatableUnit = new ArrayList<>();
                setGameAllocatableUnit(databaseDataUnitList, UNLOCKED_SOLVED, false);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (customListAdapter != null && listGameAllocatableUnit != null) {
                customListAdapter.addList(listGameAllocatableUnit);
                customListAdapter.notifyDataSetChanged();
                getListView().removeFooterView(footerView);
                loading = false;
            }
        }
    }

    protected class LoadData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... arg0) {
            while (true) {
                if (event == GAME_DESTROY || event == BACK_PRESSED)
                    break;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                openDatabase();
                if (event == GAME_DESTROY || event == BACK_PRESSED)
                    break;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                init();
                if (event == GAME_DESTROY || event == BACK_PRESSED)
                    break;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updatePendingQuery();
                if (event == GAME_DESTROY || event == BACK_PRESSED)
                    break;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getLevels();
                break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (event != GAME_DESTROY && event != BACK_PRESSED) {
                final CustomListAdapter customListAdapter = new CustomListAdapter(LevelActivity.this, listGameAllocatableUnit);
                setListAdapter(customListAdapter);
                getListView().removeFooterView(footerView);
                getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView arg0, int arg1) {
                        // nothing here
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (load(firstVisibleItem, visibleItemCount, totalItemCount)) {
                            loading = true;
                            getListView().addFooterView(footerView, null, false);
                            (new LoadPage(customListAdapter)).execute("");

                        }
                    }
                });

                findViewById(R.id.loaderView).setVisibility(View.GONE);
                getListView().setVisibility(View.VISIBLE);
                if (toastText != null && !toastText.equals(""))
                    Toast.makeText(LevelActivity.this, toastText, Toast.LENGTH_LONG).show();
            }
        }
    }
}

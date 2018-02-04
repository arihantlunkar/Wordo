package com.wordo;

/**
 * Created by Arihant on 16-03-2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wordo.game.DatabaseDataUnit;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private static DatabaseAccess instance;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;

    /**
     * Private constructor to avoid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DatabaseAccess
     */
    public static synchronized DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public synchronized void open() {
        database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public synchronized void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    /**
     * Update all pending queries of the game
     */
    public void updatePendingQuery(String pendingQuery) {
        if (database != null && !database.isOpen())
            open();
        if (database != null && database.isOpen()) {
            Cursor cursor = database.rawQuery(pendingQuery, null);
            cursor.moveToFirst();
            cursor.close();
        }
    }

    /**
     * Get no of solved levels
     */
    public int getNumberOfSolvedLevels(int level) {
        int ctr = 0;
        if (database != null && !database.isOpen())
            open();
        if (database != null && database.isOpen()) {
            Cursor cursor = database.rawQuery("SELECT Count(*) FROM dictionary where solved = 1 and " + getLevelString(level), null);
            cursor.moveToFirst();
            do {
                ctr = Integer.parseInt(cursor.getString(0));
            } while (cursor.moveToNext());
            cursor.close();
            return ctr;
        }
        return ctr;
    }


    public List<DatabaseDataUnit> returnDatabaseLevelData(int level, int solved, String limit, String orderBy) {
        List<DatabaseDataUnit> databaseDataUnitList = new ArrayList<>();
        if (database != null && !database.isOpen())
            open();
        if (database != null && database.isOpen()) {
            Cursor cursor = database.rawQuery(
                    "SELECT word,gloss,id,no_of_attempts FROM dictionary WHERE solved = "
                            + solved + " and " + getLevelString(level) + "\n" + orderBy + "\n" + limit, null);
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                cursor.close();
                return databaseDataUnitList;
            }
            do {
                DatabaseDataUnit databaseDataUnit = new DatabaseDataUnit();
                databaseDataUnit.setWord(cursor.getString(0));
                databaseDataUnit.setGloss(cursor.getString(1));
                databaseDataUnit.setWordID(cursor.getString(2));
                databaseDataUnit.setLevelNo(Integer.parseInt(cursor.getString(3)));
                databaseDataUnitList.add(databaseDataUnit);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return databaseDataUnitList;
    }

    private String getLevelString(int level) {
        String levelStr = "";
        switch (level) {
            case 0:
                levelStr = " length(word) >=3 and length(word) < 4 ";
                break;
            case 1:
                levelStr = " length(word) >=4 and length(word) < 6 ";
                break;
            case 2:
                levelStr = " length(word) >=6 and length(word) < 8 ";
                break;
            case 3:
                levelStr = " length(word) >=8 and length(word) < 10 ";
                break;
            case 4:
                levelStr = " length(word) >=10 and length(word) <= 15 ";
                break;
        }
        return levelStr;
    }
}
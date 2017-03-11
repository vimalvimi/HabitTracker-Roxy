package com.vimalroxy.habittracker_roxy.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vimalroxy.habittracker_roxy.data.HabitContract.HabitEntry;

public class HabitDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "habit.db";

    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQL_CREATE_HABIT_TABLE = "CREATE TABLE IF NOT EXISTS " + HabitEntry.TABLE_NAME + "(" +
                HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                HabitEntry.COLUMN_HABIT + " TEXT," +
                HabitEntry.COLUMN_COUNT + " INTEGER DEFAULT 0," +
                HabitEntry.COLUMN_TIME + " INTEGER DEFAULT 0" + ");";

        System.out.println("Create table " + SQL_CREATE_HABIT_TABLE);

        sqLiteDatabase.execSQL(SQL_CREATE_HABIT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
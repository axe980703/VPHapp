package com.uzflsoft.vphapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "database";
    public static final String TABLE_NAME = "guys";
    public static final String column[] = {"date", "name", "points", "type", "status"};


    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" + column[0] + " TEXT, " + column[1] + " TEXT," +
                column[2] + " TEXT," + column[3] + " TEXT," + column[4] + " TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);

        onCreate(db);
    }

    public void insertData(String[] courses) {
        SQLiteDatabase db  = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for(int i = 0; i < courses.length; i++)
            cv.put(column[i], courses[i]);

        db.insert(TABLE_NAME, null, cv);
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);

        return  res;
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }

    public boolean isTableEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean hasTables = true;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if(cursor != null && cursor.getCount() > 0){
            hasTables=false;
            cursor.close();
        }

        return hasTables;
    }



}

package com.example.wisdomapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.wisdomapp.items.Video;

import java.util.ArrayList;
import java.util.List;

public class VideoDataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "media.db";
    private static final int DATABASE_VERSION = 4;
    private static final String TABLE_NAME = "videos";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_PATH = "path";
    private static final String COL_TIMESTAMP = "timestamp";


    public VideoDataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_VIDEO_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT, "
                + COL_PATH + " TEXT, "
                + COL_TIMESTAMP + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_VIDEO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertVideo(String title, String path, String timestamp) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_PATH, path);
        contentValues.put(COL_TIMESTAMP, timestamp);

        long result = database.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllVideos() {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public List<Video> getVideoList() {
        List<Video> videoList = new ArrayList<>();

        Cursor cursor = getAllVideos();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                String title = cursor.getString(cursor.getColumnIndex(COL_TITLE));
                String path = cursor.getString(cursor.getColumnIndex(COL_PATH));
                String timestamp = cursor.getString(cursor.getColumnIndex(COL_TIMESTAMP));

                videoList.add(new Video(id, title, path, timestamp));
            }
            cursor.close();
        }

        return videoList;
    }
}


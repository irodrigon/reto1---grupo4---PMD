package com.example.wisdomapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.wisdomapp.items.Audio;
import com.example.wisdomapp.items.Image;
import com.example.wisdomapp.items.Video;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "media.db";
    private static final int DATABASE_VERSION = 19;


    private static final String TABLE_NAME_V = "videos";
    private static final String TABLE_NAME_I = "images";
    private static final String TABLE_NAME_A = "audio";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_PATH = "path";
    private static final String COL_TIMESTAMP = "timestamp";



    //DATABASE
    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //TABLES
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_VIDEO_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_V + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT, "
                + COL_PATH + " TEXT, "
                + COL_TIMESTAMP + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_VIDEO_TABLE);

        String CREATE_IMAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_I + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT, "
                + COL_PATH + " TEXT, "
                + COL_TIMESTAMP + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_IMAGE_TABLE);

        String CREATE_AUDIO_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_A + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT, "
                + COL_PATH + " TEXT, "
                + COL_TIMESTAMP + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_AUDIO_TABLE);
    }
    //DROP TABLE IF UPGRADE
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_V);
        onCreate(sqLiteDatabase);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_A);
        onCreate(sqLiteDatabase);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_I);
        onCreate(sqLiteDatabase);
    }
    //INSERTS
    public boolean insertVideo(String title, String path, String timestamp) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_PATH, path);
        contentValues.put(COL_TIMESTAMP, timestamp);

        long result = database.insert(TABLE_NAME_V, null, contentValues);
        database.close();
        return result != -1;
    }

    public boolean insertImage(String title, String path, String timestamp) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_PATH, path);
        contentValues.put(COL_TIMESTAMP, timestamp);

        long result = database.insert(TABLE_NAME_I, null, contentValues);
        database.close();
        return result != -1;
    }

    public boolean insertAudio(String title, String path, String timestamp) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_PATH, path);
        contentValues.put(COL_TIMESTAMP, timestamp);

        long result = database.insert(TABLE_NAME_A, null, contentValues);
        database.close();
        return result != -1;
    }
    //GET VIDEOS
    public Cursor getAllVideos() {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.rawQuery("SELECT * FROM " + TABLE_NAME_V, null);
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

    //GET IMAGES
    public Cursor getAllImages() {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.rawQuery("SELECT * FROM " + TABLE_NAME_I, null);
    }

    public List<Image> getImageList() {
        List<Image> imageList = new ArrayList<>();

        Cursor cursor = getAllImages();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                String title = cursor.getString(cursor.getColumnIndex(COL_TITLE));
                String path = cursor.getString(cursor.getColumnIndex(COL_PATH));
                String timestamp = cursor.getString(cursor.getColumnIndex(COL_TIMESTAMP));

                imageList.add(new Image(id, title, path, timestamp));
            }
            cursor.close();
        }

        return imageList;
    }
    //GET AUDIO
    public Cursor getAllAudios() {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.rawQuery("SELECT * FROM " + TABLE_NAME_A, null);
    }

    public List<Audio> getAudioList() {
        List<Audio> AudioList = new ArrayList<>();

        Cursor cursor = getAllAudios();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                String title = cursor.getString(cursor.getColumnIndex(COL_TITLE));
                String path = cursor.getString(cursor.getColumnIndex(COL_PATH));
                String timestamp = cursor.getString(cursor.getColumnIndex(COL_TIMESTAMP));

                AudioList.add(new Audio(id, title, path, timestamp));
            }
            cursor.close();
        }

        return AudioList;
    }

}




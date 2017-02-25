package com.example.rgb.feedme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bsheridan on 2/25/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "feedme.db", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE FeedMePosts" +
                "(eventTitle TEXT, foodType TEXT, location TEXT, latitude NUMBER," +
                "longitude NUMBER, time TEXT, description TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS FeedMePosts");
        onCreate(db);
    }
}

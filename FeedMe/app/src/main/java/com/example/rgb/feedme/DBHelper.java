package com.example.rgb.feedme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bsheridan on 2/25/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "FeedMePosts";

    public DBHelper(Context context) {
        super(context, "feedme.db", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(eventTitle TEXT, foodType TEXT, location TEXT, latitude NUMBER," +
                "longitude NUMBER, time TEXT, description TEXT, upvotes NUMBER)";
        db.execSQL(CREATE_TABLE);
        System.out.println("Created table in dbhelper");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN upvotes NUMBER");
        }
        //db.execSQL("DROP TABLE IF EXISTS FeedMePosts");
        //onCreate(db);
    }
}

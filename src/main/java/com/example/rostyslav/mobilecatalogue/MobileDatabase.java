package com.example.rostyslav.mobilecatalogue;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

public class MobileDatabase extends SQLiteOpenHelper {

    //declaration of variables for database creation and use

    public final static int VERSION = 1;
    public final static String ID = "_id";
    public final static String DATABASE_NAME = "mobile_catalogue";
    public final static String TABLE_NAME = "product";

    public final static String COLUMN_Mobile = "mobile";
    public final static String COLUMN_Model = "model";
    public final static String COLUMN_Version = "version";
    public final static String COLUMN_WWW = "www";

    public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            "(" + ID + " integer primary key autoincrement, " +
            COLUMN_Mobile + " text not null," +
            COLUMN_Model + " text not null," +
            COLUMN_Version + " text," +
            COLUMN_WWW + " text not null);";

    private static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    //adding a database designer
    public MobileDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create database
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //deleting database and create new
        db.execSQL(DELETE_TABLE);
           onCreate(db);
    }
}

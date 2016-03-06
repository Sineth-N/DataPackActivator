package com.example.sineth.datapackactivator.DatabaseAccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Sineth N on 8/10/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String CARD_TABLE = "Card";
    public static final String DATABASE_NAME = "ScratchCards.db";
    public static final int DATABASE_VERSION = 1;
    public static final String PIN = "PIN";
    public static final String ID = "id";
    public static final String VALUE = "value";


    private static final String CREATE_TABLE_CARDS = "CREATE TABLE " + CARD_TABLE + " ( " + PIN + " VARCHAR(30) NOT NULL, " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + VALUE + " INTEGER " + ");";
    private static final String DROP_TABLE_TASKS = "DROP TABLE IF EXISTS" + CARD_TABLE;
    private Context context;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CARDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_TASKS);
        onCreate(db);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {

        return super.getWritableDatabase();

    }
}

package com.example.sineth.datapackactivator.DatabaseAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sineth.datapackactivator.ScratchCard.CardInfo;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sineth on 10/8/2015.
 */
public class SMSWriterSQLite {

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private ContentValues values;
    private android.support.v7.app.AlertDialog.Builder alert;

    public SMSWriterSQLite(Context context) {
        dbHelper = new DBHelper(context);
        values = new ContentValues();
    }

    public void writeToDatabase(String PIN, int value) {
        database = dbHelper.getWritableDatabase();
        values.put(DBHelper.PIN, PIN);
        values.put(DBHelper.VALUE, value);
        Long i = 0L;
        try {
            i = database.insert(DBHelper.CARD_TABLE, null, values);
            database.close();
        } catch (Exception e) {
            Log.d("Error", "SQLite error occured");
        } finally {
            if (i == -1) {
                alert.setMessage("Error in data you entered \n please make sure that you are not duplicating records.");
                alert.setCancelable(true);
                alert.show();
            } else {
                Log.i("Success", "SQLite data entered");
//                alert.setMessage("Successfully Added to the database");
//                alert.setCancelable(true);
//                alert.show();

            }
        }
    }
    public int  deleteFromDatabase(String PIN){
        database = dbHelper.getWritableDatabase();

        int i = 0;
        try {
            i = database.delete(DBHelper.CARD_TABLE, DBHelper.PIN + "=?", new String[]{PIN});
            database.close();
        } catch (Exception e) {
            Log.d("Error", "SQLite error occured");
        } finally {
            if (i == -1) {
                alert.setMessage("Error in data you entered \n please make sure that you are not duplicating records.");
                alert.setCancelable(true);
                alert.show();
            } else {
                Log.i("Success", "SQLite data deleted");
//                alert.setMessage("Successfully Added to the database");
//                alert.setCancelable(true);
//                alert.show();
            }
            return i;
        }
    }
    public List<CardInfo> getData(Context context){
        database = dbHelper.getWritableDatabase();
//
//        SQLiteDatabase database = new DBHelper(context).getWritableDatabase();
        String[] col = {DBHelper.PIN, DBHelper.VALUE};
        List<CardInfo> contactInfos = new ArrayList<>();
        //int status = 1;
        CardInfo cardInfo;
        Cursor cursor = database.query(DBHelper.CARD_TABLE, col, null, null, null, null, null);
        while (cursor.moveToNext()) {
            cardInfo = new CardInfo();
            cardInfo.setValue(cursor.getString(cursor.getColumnIndex(DBHelper.VALUE)));
            cardInfo.setPIN(cursor.getString(cursor.getColumnIndex(DBHelper.PIN)));
            contactInfos.add(cardInfo);
        }
        database.close();
        Log.d("TAG list size", String.valueOf(contactInfos.size()));
        return contactInfos;
    }
}

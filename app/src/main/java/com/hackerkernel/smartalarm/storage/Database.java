package com.hackerkernel.smartalarm.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hackerkernel.smartalarm.pojo.SleepTimePojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by husain on 6/25/2016.
 */
public class Database extends SQLiteOpenHelper {
    private static final String TAG = Database.class.getSimpleName();
    private static String DB_NAME = "smartAlarm";
    private static int DB_VERSION = 3;

    //table schema
    private static String TABLE_SLEEP = "sleep",
            COL_SLEEP_ID = "_id",
            COL_SLEEP_FROM = "from_text",
            COL_SLEEP_TO = "to_text",
            COL_SLEEP_DATE = "date_text",
            COL_SLEEP_ACTIVE = "active";

    //Query to create table
    private String CREATE_MONEY_TABLE = "CREATE TABLE " + TABLE_SLEEP + "(" +
            COL_SLEEP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_SLEEP_FROM + " TEXT, " +
            COL_SLEEP_TO + " TEXT, " +
            COL_SLEEP_DATE + " TEXT," +
            COL_SLEEP_ACTIVE+" TEXT);";


    //update query
    private String DROP_MONEY_TABLE = "DROP TABLE IF EXISTS " + TABLE_SLEEP;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /*
    * This will add sleep time
    * but it
    * will be not active
    * */
    public long addSleepTime(String from,String to,String date){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_SLEEP_FROM,from);
        cv.put(COL_SLEEP_TO,to);
        cv.put(COL_SLEEP_DATE,date);
        cv.put(COL_SLEEP_ACTIVE,"y");

        return db.insert(TABLE_SLEEP,null,cv);
    }

    /*
    * Method to
    * */
    public void deleteLastSleep(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+COL_SLEEP_ID+" FROM "+TABLE_SLEEP+" ORDER BY "+COL_SLEEP_ID+" DESC LIMIT 1",new String[]{});
        if (cursor.moveToNext()){
            String lastId = cursor.getString(cursor.getColumnIndex(COL_SLEEP_ID));
            db.delete(TABLE_SLEEP,COL_SLEEP_ID+"=?",new String[]{lastId});
            Log.d(TAG,"HUS: delete run");
        }
        cursor.close();

    }

    public List<SleepTimePojo> getSleepTime(){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] colums = new String[]{COL_SLEEP_FROM,COL_SLEEP_TO,COL_SLEEP_DATE};
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_SLEEP+" ORDER BY "+COL_SLEEP_ID+" DESC LIMIT 5",new String[]{});
        //Cursor cursor = db.query(TABLE_SLEEP,colums,COL_SLEEP_ACTIVE+"=?",new String[]{"y"},null,null,COL_SLEEP_ID+" DESC");
        List<SleepTimePojo> list = new ArrayList<>();
        while (cursor.moveToNext()){
            SleepTimePojo pojo = new SleepTimePojo();
            String from = cursor.getString(cursor.getColumnIndex(COL_SLEEP_FROM));
            String to = cursor.getString(cursor.getColumnIndex(COL_SLEEP_TO));
            String date = cursor.getString(cursor.getColumnIndex(COL_SLEEP_DATE));

            pojo.setDate(date);
            pojo.setFrom(from);
            pojo.setTo(to);

            //add to list
            list.add(pojo);
        }
        cursor.close();
        return list;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MONEY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_MONEY_TABLE);
        onCreate(db);
    }
}

package com.fluidobjects.drafttapcontroller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Date;

/**
 * <h2>Equipment log manager</h2>
 * Provides functions for manage equipment logs
 */
public class DraftTapLog extends SQLiteOpenHelper implements BaseColumns{

    private static final String DB_NAME = "DraftTapLog";
    private static DraftTapLog sInstance;

    private DraftTapLog(Context context){
        super(context, DB_NAME, null, 1);
    }

    /**
     * <h2>Save log data in database</h2>
     */
    void save(){

    }

    static ArrayList<DraftTapLog> getLogs(Date startDate, Date endDate){
        ArrayList<DraftTapLog> arr = new ArrayList<DraftTapLog>();

        return arr;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS logs");
        onCreate(db);
    }

    static void createDatabase(Context context) {
        Date date = new Date(System.currentTimeMillis());
        date.setTime(date.getTime());
        Long.parseLong(String.valueOf(date.getTime()));
        SQLiteDatabase db = DraftTapLog.getsInstance(context).getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS logs(" +
                "data INTEGER PRIMARY KEY," +
                "servedVolume INTEGER," +
                "pulseFactor INTEGER);"
        );
    }

    static synchronized DraftTapLog getsInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DraftTapLog(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * <h2>Save log data in database</h2>
     */
    static long save(Context context,LogObj log){
        SQLiteDatabase db = DraftTapLog.getsInstance(context).getWritableDatabase();
        final String TABLE_NAME = "logs";
        long insert = -1;
        ContentValues logValues = new ContentValues();
        logValues.put("data", log.date.getTime());
        logValues.put("servedVolume", log.servedVolume);
        logValues.put("pulseFactor", log.pulseFactor);
        insert = db.insert(TABLE_NAME, null, logValues);
        db.close();
        return insert;
    }

    static ArrayList<LogObj> getLogs(Context context,long timeBegin, long timeEnd){
        SQLiteDatabase db = DraftTapLog.getsInstance(context).getWritableDatabase();
        String query;
        if(timeBegin !=0 && timeEnd!=0)
            query = "SELECT * FROM logs WHERE data BETWEEN timeBegin and timeEnd";
        else query = "SELECT * FROM logs";

        Cursor cursor      = db.rawQuery(query, null);
        ArrayList<LogObj> logs = new ArrayList<LogObj>();
        if(cursor.moveToFirst()){
            do{
                int servedVolume =cursor.getInt(cursor.getColumnIndex("servedVolume"));
                int pulseFactor= cursor.getInt(cursor.getColumnIndex("pulseFactor"));
                long time=cursor.getLong(cursor.getColumnIndex("data"));
                Date date = new Date(time);
                LogObj log = new LogObj(date,servedVolume,pulseFactor,0);
                logs.add(log);
            }while(!cursor.isLast()&& cursor.moveToNext());
        }
        else return null;
        db.close();
        return logs;
    }
}

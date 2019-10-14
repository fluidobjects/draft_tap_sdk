package com.fluidobjects.sdkchopeira;

import java.util.Date;
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
    Date date;
    int servedVolume;
    int pulseFactor;
    int remainingKegVolume;
    private static final String DB_NAME = "";
    private static final String TABLE_NAME = "";
    private static DraftTapLog sInstance;

    DraftTapLog(Context context,  Date date, int servedVolume, int pulseFactor, int remainingKegVolume){
        super(context, DB_NAME, null, 1);
    }

    private DraftTapLog(Context context){
        super(context, DB_NAME, null, 1);
    }

    /**
     * <h2>Save log data in database</h2>
     */
    public void save(){

    }

    public static ArrayList<DraftTapLog> getLogs(Date startDate, Date endDate){
        ArrayList<DraftTapLog> arr = new ArrayList<DraftTapLog>();

        return arr;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TABLE_NAME");
        onCreate(db);
    }

//    public MyDBhelper(Context context){
//        super(context, DB_NAME, null, 1);
//    }

    public static void criaBancos(Context context) {
//        Date date = new Date(System.currentTimeMillis());
//        date.setTime(date.getTime());
        SQLiteDatabase db = DraftTapLog.getsInstance(context).getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS equipments(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "serial TEXT," +
                "size TEXT," +
                "current_temperature INTEGER," +
                "programmed_temperature INTEGER," +
                "associated BOOLEAN," +
                "isOn BOOLEAN," +
                "connected BOOLEAN," +
                "ip STRING," +
                "device_id STRING," +
                "operating BOOLEAN);"
        );
    }

    public static synchronized DraftTapLog getsInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DraftTapLog(context.getApplicationContext());
        }
        return sInstance;
    }

    public static long InsertLogs(Context context,ArrayList<DraftTapLog> logs, long lastLogTime){
        SQLiteDatabase db = DraftTapLog.getsInstance(context).getWritableDatabase();
        final String TABLE_NAME = "logs";
        long insert = -1;
        for(DraftTapLog log : logs) {
//            if( log.getDate().getTime() > lastLogTime) {
//                ContentValues logValues = new ContentValues();
//                logValues.put("id", log.getId());
//                logValues.put("currentTemp", log.getCurrentTemp());
//                logValues.put("programedTemp", log.getProgrammedTemp());
//                logValues.put("heaterOn", log.getHeaterOn());
//                logValues.put("time", log.getDate().getTime());
//                insert = db.insert(TABLE_NAME, null, logValues);
//            }
        }
        db.close();
        return insert;
    }

    public static ArrayList<DraftTapLog> returnLogs(Context context, int id){
        SQLiteDatabase db = DraftTapLog.getsInstance(context).getWritableDatabase();
        String query = "SELECT * FROM logs WHERE id="+id+";";
        Cursor cursor      = db.rawQuery(query, null);
        ArrayList<DraftTapLog> logs = new ArrayList<DraftTapLog>();
        if(cursor.moveToFirst()){
            do{
                int programedTemp= cursor.getInt(cursor.getColumnIndex("programedTemp"));
                int currentTemp =cursor.getInt(cursor.getColumnIndex("currentTemp"));
                Boolean heaterOn= cursor.getInt(cursor.getColumnIndex("heaterOn")) > 0;
                long time=cursor.getLong(cursor.getColumnIndex("time"));
                Date date = new Date(time);
//                DraftTapLog log = new DraftTapLog(id,date,heaterOn,currentTemp,programedTemp);
//                logs.add(log);
            }while(!cursor.isLast()&& cursor.moveToNext());
        }
        else return null;
        db.close();
        return logs;
    }


}

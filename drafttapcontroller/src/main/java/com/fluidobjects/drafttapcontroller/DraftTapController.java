package com.fluidobjects.drafttapcontroller;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

/**
 * <h2>Controls Draft Tap Equipment</h2>
 * Provides functions for manage equipment functionality, logs and keg consume
 */
public class DraftTapController{
    private String ip;
    private int pulseFactor = 5000;
    private Equipment equipment;
    private Context context;
    private int lastVolumeRead = 0;
    private int cutVolume = 0;
    private  boolean enabled = true;

    /**
     * @param ip      String of ip adress of equipment for open connection.
     * @param context Context of the running Activity.
     */
    public DraftTapController(Context context, String ip) throws Exception {
        equipment = new Equipment(ip);
        this.ip = ip;
        this.context = context;
        verifyEquip();
        if(enabled) {
            DraftTapLog.createDatabase(context);
            SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
            pulseFactor = sharedPreferences.getInt("pulseFactor", pulseFactor);
            cutVolume = sharedPreferences.getInt("cutVolume", cutVolume);
        }else throw new Exception("Could not open connection with device ip: "+ip);
    }

    /**
     * Use the readVolume and expected volume to recalculate pulseFactor.
     * Should be used when the measurement of volume served is wrong.
     *
     * @param readVolume Number in ml. Volume served in calibration tests.
     */
    public void calibratePulseFactor(int readVolume) {
        print("Old factor " + pulseFactor);
        pulseFactor = (lastVolumeRead * pulseFactor) / readVolume;
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("pulseFactor", pulseFactor);
        editor.apply();
        print("New factor " + pulseFactor);
    }

    /**
     * <h2>Read volume</h2>
     * Read the served volume
     *
     * @return false in case of error, true otherwise
     */
    public int readVolume() {
        return equipment.getVolume();
    }

    public float remainingKegVolume() {
        return 0.0f;
    }

    /**
     * @return List of all saved log objects.
     */
    public ArrayList<LogObj> getLogs() {
        return DraftTapLog.getLogs(context, 0, 0);
    }

    /**
     * @return List of log objects between startDate and endDate
     */
    public ArrayList<LogObj> getLogs(Date startDate, Date endDate) {
        return DraftTapLog.getLogs(context, startDate.getTime(), endDate.getTime());
    }

    /**
     * @param context Context of the running Activity
     * @return List of all saved log objects.
     */
    public static ArrayList<LogObj> getLogs(Context context) {
        return DraftTapLog.getLogs(context, 0, 0);
    }

    /**
     * @param context Context of the running Activity
     * @return List of log objects between startDate and endDate
     */
    public static ArrayList<LogObj> getLogs(Context context, Date startDate, Date endDate) {
        return DraftTapLog.getLogs(context, startDate.getTime(), endDate.getTime());
    }

    /**
     * <h2>Open Valve</h2>
     * Open valve so user can start serving before servingTimeout ends.
     * The valve will close when user stop serving or maxVolume reached
     *
     * @param maxVolume Number in ml. The maximum volume user is allowed to serve.
     */
    public void openValve(int maxVolume, boolean isCalibrating) throws Exception {
        verifyEquip();
        print("Serving " + equipment.isServing());
        if (equipment.open(pulseFactor, isCalibrating ? maxVolume : maxVolume - cutVolume)) {
            DraftTapLog.save(context, new LogObj(new Date(), maxVolume, pulseFactor, 0));
            lastVolumeRead = equipment.monitorsVolume();
            if (!isCalibrating) {
                cutVolume = lastVolumeRead - maxVolume;
                SharedPreferences.Editor editor = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE).edit();
                editor.putInt("cutVolume", cutVolume);
                editor.apply();
            }
        } else throw new Exception("Failed opening Equipment");
    }

    private static void print(String text){
        Log.d("DraftController", text + "\n");
    }

    private void equipIsEnabled(){
        try {
            String mac = equipment.getMacFromArp(ip);
            SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
            String dis = sharedPreferences.getString("disabled", "");
            if(dis.contains(mac)) enabled = false;
            enabled = true;
        }catch (Exception e){enabled = true; }
    }

    private void verifyEquip(){
        equipIsEnabled();
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        long timeLastVerify = sharedPreferences.getLong("timeLastVerify", 0);
        if((new Date()).getTime() - timeLastVerify > 86400000 || !enabled){//mais de 1 dia
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putLong("timeLastVerify", (new Date()).getTime());
            editor.apply();
            Equipment.request(context,ip);
        }
    }
}
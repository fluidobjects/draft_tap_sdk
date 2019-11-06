package com.fluidobjects.drafttapcontroller;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.ArrayList;
import java.util.Date;

/**
 * <h2>Controls Draft Tap Equipment</h2>
 * Provides functions for manage equipment functionality and logs
 */
public class DraftTapController{
    private String ip;
    private int pulseFactor = 700;
    private Equipment equipment;
    private Context context;
    private int lastVolumeRead = 0;
    public int cutVolume = 13;
    private  boolean enabled = true;
    public  boolean isServing = false;

    /**
     * <h2>DraftTapController</h2>
     * @param ip      String of ip address of equipment for open connection.
     * @param context Context of the running Activity.
     */
    public DraftTapController(Context context, String ip) throws Exception {
        equipment = new Equipment(ip);
        this.ip = ip;
        this.context = context;
        DraftTapLog.createDatabase(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        pulseFactor = sharedPreferences.getInt("pulseFactor", pulseFactor);
        verifyEquip();
    }

    /**
     * Use the readVolume and expected volume to recalculate pulseFactor.
     * Should be used when the measurement of volume served is wrong.
     *
     * @param measuredVolume Number in ml. Volume expected in calibration tests.
     * @param servedVolume Number in ml. Volume served in calibration tests.
     */
    public void calibratePulseFactor(int measuredVolume, int servedVolume) {
        pulseFactor = (measuredVolume * pulseFactor) / servedVolume;
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("pulseFactor", pulseFactor);
        editor.apply();
        print("New pulse factor " + pulseFactor);
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
    public void openValve(int maxVolume) throws Exception {
        verifyEquip();
        if(!enabled) throw new Exception("This equipment is not enabled. Contact support@fluidobjects.com.");
        isServing=true;
        if (equipment.open(pulseFactor,(((int) (maxVolume - (cutVolume + (maxVolume * 0.025))))))){
            DraftTapLog.save(context, new LogObj(new Date(), maxVolume, pulseFactor, (int)(cutVolume +(maxVolume * 0.025))));
            lastVolumeRead = equipment.monitorsVolume();
            isServing=false;
        } else{
            isServing=false;
            throw new Exception("Failed opening Equipment");
        }
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                equipIsEnabled();
                SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
                long timeLastVerify = sharedPreferences.getLong("timeLastVerify", 0);
                if((new Date()).getTime() - timeLastVerify > 86400000 || !enabled){//mais de 1 dia
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putLong("timeLastVerify", (new Date()).getTime());
                    editor.apply();
                    Equipment.request(context,ip);
                }
            }}).start();
    }

    /**
     * <h2>setTimeouts</h2>
     * Open valve so user can start serving before servingTimeout ends.
     * The valve will close when user stop serving or maxVolume reached
     *
     * @param beginTimeout Number in milliseconds. The time waited before user start to serve to close the valve.
     * @param servingTimeout Number in milliseconds. The time waited after user start to serve to close the valve.
     */
    public void setTimeouts(int beginTimeout, int servingTimeout)throws Exception{
        try {
            equipment.setTimeoutGeral(beginTimeout);
            equipment.setTimeoutIntermediario(servingTimeout);
        }catch (Exception e){
            throw new Exception("Error while changing timeout values");
        }
    }
}
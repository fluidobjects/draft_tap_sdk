package com.fluidobjects.sdkchopeira;

import android.arch.core.util.Function;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <h2>Controls Draft Tap Equipment</h2>
 * Provides functions for manage equipment functionality, logs and keg consume
 */
public class DraftTapController {
    private String ip;
    private int pulseFactor = 5000;
    private Equipment equipment;
    private Context context;

    /**
     * @param ip String of ip adress of equipment for open connection
     */
    DraftTapController(Context context, String ip){
        this.ip = ip;
        equipment = new Equipment(ip);
        this.context = context;
        DraftTapLog.createDatabase(context);
    }

    /**
     * @param ip String of ip adress of equipment for open connection.
     * @param pulseFactor Number used to convert pulses to ml(default is 5000).
     */
    DraftTapController(Context context, String ip, int pulseFactor){
        equipment = new Equipment(ip);
        this.ip = ip;
        this.pulseFactor = pulseFactor;
        this.context = context;
        DraftTapLog.createDatabase(context);
    }

    /**
     * Use the readVolume and expected volume to recalculate pulseFactor.
     * Should be used when the measurement of volume served is wrong.
     *
     * @param expectedVolume Number in ml. Volume wanted to serve. Usually the maxVolume in calibration tests.
     * @param readVolume Number in ml. Volume served in calibration tests.
     */
    public void calibratePulseFactor(int expectedVolume, int readVolume){
        print("Old factor" + pulseFactor);
        pulseFactor = (expectedVolume * pulseFactor)/readVolume;
        print( "New factor" + pulseFactor);
    }

    /**
     * <h2>Read volume</h2>
     * Read the served volume 
     * @return false in case of error, true otherwise
     */
    public int readVolume(){
        return equipment.getVolume();
    }

    public float remainingKegVolume(){
        return 0.0f;
    }

    /**
     * @return List of all saved log objects.
     */
    public List<LogObj> getLogs(){
        return DraftTapLog.getLogs(context,0,0);
    }

    /**
     * @return List of log objects between startDate and endDate
     */
    public List<LogObj> getLogs(Date startDate, Date endDate){
        return DraftTapLog.getLogs(context,startDate.getTime(),endDate.getTime());
    }

    /**
     * <h2>Open Valve</h2>
     * Open valve so user can start serving before servingTimeout ends.
     * The valve will close when user stop serving or maxVolume reached
     * @param maxVolume Number in ml. The maximum volume user is allowed to serve.
     */
    public boolean openValve(int maxVolume){
        print("Serving " + equipment.isServing());
        if(equipment.open(pulseFactor,maxVolume)){
            DraftTapLog.save(context,new LogObj(new Date(),maxVolume,pulseFactor,0));
            Thread t = new Thread(new Runnable() {
                public void run() {
                    equipment.monitorsVolume();
                }
            });
            t.start();
            return true;
        }
        print("Falha comunicação com Equipamento");
        return false;
    }

    private void print(String text){
        Log.d("DraftController", text);
    }

//    public void initializeKeg(float newVolume){
//        //currentData
//    }

    /**
     * <h2>Open Valve</h2>
     * Open valve so user can start serving before servingTimeout ends.
     * The valve will close when user stop serving or maxVolume reached.
     * @return false in case of error, true otherwise
     */
//    public boolean openValve(){
//        print("Factor" + String.valueOf(pulseFactor));
//        if(!equipment.open(pulseFactor, maxVolume)){
//            print(" Falha comunicação CLP");
//            return false;
//        }
//        Thread t = new Thread(new Runnable() {
//            public void run() {
//                equipment.monitorsVolume();
//            }
//        });
//        t.start();
//        return true;
//    }
}
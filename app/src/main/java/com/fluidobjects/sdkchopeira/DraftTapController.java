package com.fluidobjects.sdkchopeira;

import android.arch.core.util.Function;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <h2>Controls Draft Tap Equipment</h2>
 * Provides functions for manage equipment functionality, logs and keg consume
 */
public class DraftTapController {
    private int maxVolume=0;
    private String ip;
    private float servingTimeout = 3.0f;
    private int pulseFactor = 5000;
    private Equipment equipment;

    /**
     * @param ip String of ip adress of equipment for open connection
     */
    DraftTapController(String ip){
        this.ip = ip;
        equipment = new Equipment(ip);
    }

    /**
     * @param ip String of ip adress of equipment for open connection.
     * @param servingTimeout Float number in milliseconds. The time the valve remains open after the user stops serving.
     * @param pulseFactor Number used to convert pulses to ml(default is 5000).
     */
    DraftTapController(String ip, float servingTimeout, int pulseFactor){
        equipment = new Equipment(ip);
//        equipment.setMaxVol(maxVolume);
        this.ip = ip;
        this.servingTimeout = servingTimeout;
        this.pulseFactor = pulseFactor;
    }

    /**
     * Use the readVolume and expected volume to recalculate pulseFactor.
     * Should be used when the measurement of volume served is wrong.
     *
     * @param expectedVolume Number in ml. Volume wanted to serve. Usually the maxVolume in calibration tests.
     * @param readVolume Number in ml. Volume served in calibration tests.
     */
    public void calibratePulseFactor(int expectedVolume, int readVolume){
        print("Old factor" + String.valueOf(pulseFactor));
        pulseFactor = (expectedVolume * pulseFactor)/readVolume;
        print( "New factor" + String.valueOf(pulseFactor));
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
    public List<DraftTapLog> getLogs(){
        return new ArrayList<DraftTapLog>(0);
    }

    /**
     * @return List of log objects between startDate and endDate
     */
    public List<DraftTapLog> getLogs(Date startDate, Date endDate){
        return new ArrayList<DraftTapLog>(0);
    }

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

    /**
     * <h2>Open Valve</h2>
     * Open valve so user can start serving before servingTimeout ends.
     * The valve will close when user stop serving or maxVolume reached
     * @param maxVolume Number in ml. The maximum volume user is allowed to serve.
     */
    public boolean openValve(int maxVolume){
        print("Factor" + String.valueOf(pulseFactor));
        if(equipment.open(pulseFactor,maxVolume)){
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
}
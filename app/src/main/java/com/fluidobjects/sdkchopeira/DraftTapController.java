package com.fluidobjects.sdkchopeira;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <h2>Controls Draft Tap Equipment</h2>
 * Provides functions for manage equipment functionality, logs and keg consume
 */
public class DraftTapController {
    public float maxVolume=0;
    public String ip;
    public float servingTimeout = 3.0f;
    public int pulseFactor = 5000;
    private Equipment equipment;

    /**
     * @param ip String of ip adress of equipment for open connection
     */
    DraftTapController(String ip){
        this.ip = ip;
    }

    /**
     * @param ip String of ip adress of equipment for open connection.
     * @param maxVolume Number in ml. The maximum volume user is allowed to serve.
     * @param servingTimeout Float number in milliseconds. The time the valve remains open after the user stops serving.
     * @param pulseFactor Number used to convert pulses to ml(default is 5000).
     */
    DraftTapController(String ip, float maxVolume, float servingTimeout, int pulseFactor){
        equipment = new Equipment(ip);
        this.ip = ip;
        this.maxVolume = maxVolume;
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

    }

    public void initializeKeg(float newVolume){
        //currentData
    }

    public float readVolume(){
        return 0.0f;
    }

    public float remainingKegVolume(){
        return 0.0f;
    }

    public List<DraftTapLog> getLogs(){
        return new ArrayList<DraftTapLog>(0);
    }

    public List<DraftTapLog> getLogs(Date startDate, Date endDate){
        return new ArrayList<DraftTapLog>(0);
    }

    /**
     * <h2>Open Valve</h2>
     * Open valve so user can start serving before servingTimeout ends.
     * The valve will close when user stop serving or maxVolume reached
     */
    public boolean openValve(){
        return false;
    }

    /**
     * <h2>Open Valve</h2>
     * Open valve so user can start serving before servingTimeout ends.
     * The valve will close when user stop serving or maxVolume reached
     * @param maxVolume Number in ml. The maximum volume user is allowed to serve.
     */
    public boolean openValve(int maxVolume){
        return false;
    }
}

//METHODS
/**
 * This is the main method which makes use of addNum method.
 * @param args Unused.
 * @return Nothing.
 * @exception IOException On input error.
 * @see IOException
 */

//CLASSES
/**
 * <h1>Add Two Numbers!</h1>
 * The AddNum program implements an application that
 * simply adds two given integer numbers and Prints
 * the output on the screen.
 * <p>
 * <b>Note:</b> Giving proper comments in your program makes it more
 * user friendly and it is assumed as a high quality code.
 *
 * @author  Zara Ali
 * @version 1.0
 * @since   2014-03-31
 */
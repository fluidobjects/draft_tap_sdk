package com.fluidobjects.drafttapcontroller;

import java.util.Date;

/**
 * <h2>Equipment log object</h2>
 * Object saved in database
 */
public class LogObj{
    public Date date;
    public int servedVolume;
    public int pulseFactor;
    public int cutVolume;

    public LogObj(Date date, int servedVolume, int pulseFactor,int cutVolume){
        this.date = date;
        this.servedVolume = servedVolume;
        this.pulseFactor = pulseFactor;
        this.cutVolume = cutVolume;
    }
}

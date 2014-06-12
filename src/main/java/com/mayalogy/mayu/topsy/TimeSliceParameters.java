package com.mayalogy.mayu.topsy;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Parameters defining a sample window.
 */
public class TimeSliceParameters {

    private int sizeInMinutes = 1;
    private int numberOfTimeSlices=1;
    private long topsyMinimumTime=-1;
    private long topsyMaximumTime=-1;
    private long trialStartTime = -1;
    
    public TimeSliceParameters(int sizeInMinutes, int numberOfWindows) {
        this.topsyMaximumTime=System.currentTimeMillis()/1000-300; //start 5 min back due to Topsy lag.
        init(sizeInMinutes, numberOfWindows);
    }
    
    public TimeSliceParameters(int sizeInMinutes, int numberOfWindows, String startTime) throws ParseException {
        this.topsyMaximumTime=TimeSlice.DATE_FORMAT.parse(startTime).getTime()/1000;
        init(sizeInMinutes, numberOfWindows);
    }
    
    private void init(int sim, int nW) {
        this.sizeInMinutes=sim;
        this.numberOfTimeSlices=nW;
        this.topsyMinimumTime=topsyMaximumTime-this.sizeInMinutes*60;
        this.trialStartTime=this.topsyMaximumTime;
    }
    
    public int getSizeInMinutes() {
        return this.sizeInMinutes;
    }
    
    public int getNumberOfTimeSlices() {
        return this.numberOfTimeSlices;
    }
    
    public long getTopsyMinTime() {
        return this.topsyMinimumTime;
    }
    
    public Date getMinTime() {
        return new Date(this.topsyMinimumTime*1000);
    }
    
    public void setTopsyMaxTime(long timeInSeconds) {
        this.topsyMaximumTime=timeInSeconds;
    }
    
    public long getTopsyMaxTime() {
        return this.topsyMaximumTime;
    }
    
    public String getDisplayableTrialStartTime() {
        return TimeSlice.DATE_FORMAT.format(new Date(1000*this.trialStartTime));    
    }
    
    public String getDisplayableTopsyMinimumTime() {
        return TimeSlice.DATE_FORMAT.format(new Date(1000*this.topsyMinimumTime));    
    }
    
    public String getDisplayableTopsyMaximumTime() {
        return TimeSlice.DATE_FORMAT.format(new Date(1000*this.topsyMaximumTime));    
    }
    
    public void shiftBackInTime() {
        this.topsyMaximumTime=this.topsyMaximumTime - this.sizeInMinutes*60;
        this.topsyMinimumTime=topsyMaximumTime - this.sizeInMinutes*60;
    }
    
    public List<TimeSliceParameters> slice() {
        List<TimeSliceParameters> sampleWindows = new ArrayList<TimeSliceParameters>();
        TimeSliceParameters ts1 = new TimeSliceParameters(this.sizeInMinutes/2, this.numberOfTimeSlices);
        ts1.topsyMinimumTime=this.topsyMinimumTime;
        ts1.topsyMaximumTime=((this.topsyMaximumTime-this.topsyMinimumTime)/2)+this.topsyMinimumTime;
        ts1.trialStartTime = this.trialStartTime;
        
        TimeSliceParameters ts2 = new TimeSliceParameters(this.sizeInMinutes/2, this.numberOfTimeSlices);
        ts2.topsyMinimumTime=ts1.topsyMaximumTime;
        ts2.topsyMaximumTime=this.topsyMaximumTime;
        ts2.trialStartTime=ts2.trialStartTime;
        
        sampleWindows.add(ts1);
        sampleWindows.add(ts2);
        return sampleWindows;
    }
}

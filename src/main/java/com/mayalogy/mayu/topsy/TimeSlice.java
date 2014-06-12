package com.mayalogy.mayu.topsy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class TimeSlice implements Serializable {

    private static final long serialVersionUID = 1L;

    private Topic topic;
    private long startTimeInSeconds=-1;
    private List<StatusUpdate> updates=new ArrayList<StatusUpdate>();
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
    private static Logger logger = Logger.getLogger(TimeSlice.class.getName());

    TimeSlice(Topic topic) {
        this.topic=topic;
    }
    
    void setStartTimeInSeconds(long s) {
        this.startTimeInSeconds=s;
    }
    
    public String getStartTime() {
        return DATE_FORMAT.format(new Date(1000*this.startTimeInSeconds));
    }
    
    public long getStartTimeInSeconds() {
        return this.startTimeInSeconds;
    }
    
    static List<TimeSlice> load(File updatesDir) throws Exception {
        List<TimeSlice> windows = new ArrayList<TimeSlice>();
        File path;
        for(String tt:updatesDir.list()) {
            path=new File(updatesDir.getAbsolutePath()+"/"+tt);
            for(String t:path.list()) {
                File topicPath=new File(path.getAbsolutePath()+"/"+t);
                addWindowData(topicPath, windows);
            }
        }
        return windows;
    }
    
    private static void addWindowData(File path, List<TimeSlice> windows) {
        for(File f:path.listFiles()) {
            if(f.isDirectory()) {
                addWindowData(f,windows);
            } else {
                System.out.println("Loading time slice of status updates from: " + path.getAbsolutePath());
                try {
                    FileInputStream fin = new FileInputStream(path.getAbsolutePath()+"/updates.dat");
                    ObjectInputStream ois = new ObjectInputStream(fin);
                    windows.add((TimeSlice) ois.readObject());
                    ois.close();
                    fin.close();
                } catch (Exception e) { 
                    logger.warn(e);
                }
            }
        }
    }
    
    public List<StatusUpdate> getUpdates() {
        return this.updates;
    }
    
    public Topic getTopic() {
        return this.topic;
    }
    
    void addStatusUpdate(StatusUpdate u) {
        this.updates.add(u);
    }
    
    public static List<String> getTopicNames(List<TimeSlice> windows) {
        Set<String> topics = new HashSet<String>();
        for(TimeSlice w:windows) {
            topics.add(w.getTopic().getName());
        }
        return new ArrayList<String>(topics);
    }
    
    public static List<StatusUpdate> getStatusUpdates(List<TimeSlice> windows) {
        List<StatusUpdate> updates = new ArrayList<StatusUpdate>();
        for(TimeSlice w:windows) {
            updates.addAll(w.getUpdates());
        }
        return updates;
    }
    
    public static void sort( List<TimeSlice> timeSlice ) {
        TimeSliceComparator comparator =
            TimeSliceComparator.getInstance();
        Collections.sort(timeSlice, comparator);
    }
    
    public String toReadableString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Time slice starting: " + getStartTime());
        sb.append(" for topic: " + topic.getName());
        return sb.toString();
    }
    
    public static List<TimeSlice> loadFromFile(String fileName, Topic topic) throws Exception {
        Map<Topic, TimeSlice> topicTimeSliceMap = new HashMap<Topic, TimeSlice>();
        File file = new File(fileName);
        logger.info("Loading "+fileName+" file from: " + fileName);

        BufferedReader bufferedSource = new BufferedReader (new FileReader(file));
        String line;
        while ((line = bufferedSource.readLine()) != null) {
            if (line.trim().length() > 0) {
                if(line.startsWith("#"))
                    continue;
                String[] cols = line.split("\\|");
                Topic t = new Topic(cols[0]);
                if(t==topic || topic.isAll()) {
                    StatusUpdate u = new StatusUpdate();
                    u.setContent(cols[2]);
                    u.setFirstPostDate(TimeSlice.DATE_FORMAT.parse(cols[1]));
                    if(!topicTimeSliceMap.containsKey(t)) {
                        topicTimeSliceMap.put(t, new TimeSlice(t));
                    }
                    topicTimeSliceMap.get(t).addStatusUpdate(u);
                }
            } 
        }
        bufferedSource.close();
        return new ArrayList<TimeSlice>(topicTimeSliceMap.values());
    }
}

class TimeSliceComparator implements Comparator<TimeSlice> {
    
    TimeSliceComparator(){
        super();
    }

    public static TimeSliceComparator getInstance(){
        return new TimeSliceComparator();
    }

    public int compare( TimeSlice wd1, TimeSlice wd2 ) {
        return (int)(wd1.getStartTimeInSeconds()-wd2.getStartTimeInSeconds());
    }
}

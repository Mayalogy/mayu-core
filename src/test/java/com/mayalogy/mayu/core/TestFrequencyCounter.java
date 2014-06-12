package com.mayalogy.mayu.core;

import junit.framework.TestCase;

public class TestFrequencyCounter extends TestCase {
    
    public void testUpdate() {
        FrequencyCounter fc = new FrequencyCounter();
        fc.update("hello", 1);
        fc.update("hello", 1);
        fc.update("hello2", 1);
        fc.update("hello3", 1);
        float totalCount = 0; float totalPercent = 0;
        for(Term t:fc.getAllRankedByFrequency()) {
            System.out.println(t.getCount() + " " + t.getPercentOfTotalCounted());
            totalCount=totalCount+t.getCount();
            totalPercent=totalPercent+t.getPercentOfTotalCounted();
        }
        assertTrue((int)totalCount==4);
        assertTrue((int)totalPercent==100);
    }

}

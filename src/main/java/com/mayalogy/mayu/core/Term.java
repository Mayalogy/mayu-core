package com.mayalogy.mayu.core;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Term implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String value = null;
    private float count = 0;
    private float totalCounted = -1;
    
    void setTotalCounted(float m) {
        this.totalCounted=m;
    }
    
    public float getPercentOfTotalCounted() {
        return 100*count/totalCounted;
    }
    
    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    
    /**
     * @param value the value to set
     */
    void setValue(String value) {
        this.value = value;
    }
    
    public Term(String value) {
        this.value=value;
    }
    
    /**
     * @return the count
     */
    public float getCount() {
        return count;
    }
    
    void increaseCount(float amount) {
        count=count+amount;
    }
    
    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }
    
    /**
     * @param count the count to set
     */
    public void setCount(float count) {
        this.count = count;
    }
    
    public static void sort( List<Term> counters ) {
        TermCountComparator comparator =
            TermCountComparator.getInstance();
        Collections.sort(counters, comparator);
    }
}

class TermCountComparator implements Comparator<Term> {
    
    TermCountComparator(){
        super();
    }

    public static TermCountComparator getInstance(){
        return new TermCountComparator();
    }

    public int compare( Term r1, Term r2 ) {
        return (int)(1000000*r2.getCount()-1000000*r1.getCount());
    }
}


package com.mayalogy.mayu.io;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StringSorter {
    
    public static void sortByLength(List<String> strList, boolean increasingLength) {
        StringLengthComparator slc = StringLengthComparator.getInstance(increasingLength);
        Collections.sort(strList, slc);
    }
}

class StringLengthComparator implements Comparator<String> {
        
    private boolean increasingLength;
    
    StringLengthComparator(boolean increasingLength){
        super();
        this.increasingLength=increasingLength;
    }

    public static StringLengthComparator getInstance(boolean increasingLength){
        return new StringLengthComparator(increasingLength);
    }

    public int compare( String r1, String r2 ) {
        if(increasingLength)
            return r1.length()-r2.length();
        else
            return r2.length()-r1.length();
    }
}
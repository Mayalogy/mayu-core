package com.mayalogy.mayu.core;

import java.util.ArrayList;
import java.util.List;

public class Centroid {

	/**
	 * Generates a term frequency centroid given a blob of whitespace separated text, i.e. white space separated sentences.
	 */
    public static List<Term> generate(String blob) {
    	List<Term> centroid = new ArrayList<Term>();
        if(blob==null || blob.length()<1)
            return centroid;

        FrequencyCounter fc = new FrequencyCounter();
        for(String word:WordGramExtractor.getGrams(1, blob)) {
        	fc.update(word);
        }
        return fc.getAll();
    } 
    
}

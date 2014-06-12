package com.mayalogy.mayu.core;

import java.util.List;

import junit.framework.TestCase;

public class TestWordGramExtractor extends TestCase {
    
    public void testGetGrams() {
        List<String> grams = WordGramExtractor.getGrams(1, "The pig was is and a.");
        System.out.println(grams.toString());
        assertTrue(grams.contains("pig"));
        assertTrue(!grams.contains("The"));
        
        grams = WordGramExtractor.getGrams(2, "I visited the United Nations on Tuesday.");
        System.out.println(grams.toString());
        assertTrue(grams.contains("United Nations"));
        
        grams = WordGramExtractor.getGrams(3, "I visited the United Nations on Tuesday.");
        System.out.println(grams.toString());
        assertTrue(grams.contains("the United Nations"));
        
        grams = WordGramExtractor.getGrams(9, "I visited the United Nations on Tuesday.");
        System.out.println(grams.toString());
        assertTrue(grams.size()==0);
    }
}

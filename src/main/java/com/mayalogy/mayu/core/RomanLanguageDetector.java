package com.mayalogy.mayu.core;

import java.util.List;

/**
 * Simple quick and dirty roman language detector. Looks for . 50% of characters are in a simple non-accented english character set.
 */
public class RomanLanguageDetector {

    private static String ROMAN_CHARS = "abcedfghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_\"'()[]";
    
    public static boolean isRoman(String phrase) {
        if(phrase==null||phrase.length()<1)
            return false;
        float numRoman=0;
        for(int i=0; i<phrase.length(); i++) {
            if(ROMAN_CHARS.contains(""+phrase.charAt(i))) {
                numRoman++;
            }
        }
        if(numRoman/(float)phrase.length()>.5)
            return true;
        
        return false;
    }
    
	/**
	 * Super simple english detector that returns true if the ratio of english stop words to total words is > 5%.
	 */
	public static boolean isEnglish(String t) {
		float numStops = 0;
		List<String> words = WordGramExtractor.get1Gram(t, true);
		for(String w:words) {
			if(StopWords.getInstance().contains(w))
				numStops++;
		}
		return (numStops/words.size())>.05f;
	}
}

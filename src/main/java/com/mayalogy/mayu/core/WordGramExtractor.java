package com.mayalogy.mayu.core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Extracts word N grams from text.
 */
public class WordGramExtractor {

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    private static String trailingBlacklistedChars = ".!,-;?')]:\"”";
    private static String precedingBlacklistedChars = "['“(\"";
    
    public static void setTrailingBlacklistedChars(String tbc) {
    	WordGramExtractor.trailingBlacklistedChars=tbc;
    }
    
    public static void setPreceedingBlacklistedChars(String pbc) {
    	WordGramExtractor.precedingBlacklistedChars=pbc;
    }
    
	/**
	 * Returns word grams from the input string
	 * @param max The max number of word grams. I.e. max=2 means 1 and 2 word grams are returned. Max must be a number greater than 1.
	 */
	public static List<String> getGramsInRange(int max, String input) {
		if(max<2)
			throw new IllegalArgumentException("max must be a number greater than 1.");

		List<String> grams = new ArrayList<String>();
		for(int i=max; i>0; i--) {
			for(String g:WordGramExtractor.getGrams(i, input)) {
				grams.add(g);
			}
		}
		return grams;
	}
	
    /**
     * Returns word grams from a body of text. Stop words are retained except in the single word gram case.
     * @param numberOfGrams The number of grams.
     * @param input The text.
     * @return A list of word grams.
     */
    public static List<String> getGrams(int numberOfGrams, String input) {
        if(numberOfGrams<1)
            throw new IllegalArgumentException("Number of word grams must be > 0");
        
        if(numberOfGrams==1)
            return get1Gram(input, false);
        
        String[] nthGram = new String[numberOfGrams];
        List<String> termsWStopWords=get1Gram(input, true);
        List<String> grams = new ArrayList<String>(termsWStopWords.size());
        if(termsWStopWords.size()<numberOfGrams)
            return new ArrayList<String>();
        
        for(String t:termsWStopWords) {
            boolean noGrams = true;
            for(int i=1; i<numberOfGrams; i++) {
                if(nthGram[i]!=null) {
                    noGrams=false;
                } else {
                    noGrams = true;
                    break;
                }
            }
            if(!noGrams) {
                StringBuilder sb = new StringBuilder();
                for(int i=numberOfGrams; i>1; i--) {
                    sb.append(nthGram[i-1]);
                    sb.append(" ");
                }
                sb.append(t);
                grams.add(sb.toString());
            }
            for(int i=numberOfGrams; i>1; i--) {
                nthGram[i-1]=nthGram[i-2];
            }
            nthGram[1]=t;
        }
        return grams;
    }
    
    public static List<String> get1Gram(String input, boolean leaveStopWords) {
        List<String> terms = new ArrayList<String>();
        String[] candidates = WHITESPACE_PATTERN.split(input);
        for(String s:candidates) {
            s = s.trim();
            s = removeTrailingBlacklistedChars(s);
            s = removePrecedingBlacklistedChars(s);
            if(s.length()>0) {
                if(leaveStopWords)
                    terms.add(s);
                else if (!StopWords.getInstance().contains(s.toLowerCase()))
                    terms.add(s);
            }
        }
        return terms;
    }

    public static String removeTrailingBlacklistedChars(String s) {
        if(s.length()<2)
            return s;
        if(trailingBlacklistedChars.indexOf(s.charAt(s.length()-1))==-1) {
            return s;
        } else {
            return removeTrailingBlacklistedChars(s.substring(0, s.length()-1));
        }
    }

    private static String removePrecedingBlacklistedChars(String s) {
        if(s.length()<2)
            return s;
        if(precedingBlacklistedChars.indexOf(s.charAt(0))==-1) {
            return s;
        } else {
            return removePrecedingBlacklistedChars(s.substring(1, s.length()));
        }
    }
} 

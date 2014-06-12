package com.mayalogy.mayu.core;

public class StopWords extends LookupSet {

    private static StopWords instance = null;

    private StopWords() {
        try {
            loadJarResource("com/mayalogy/mayu/core/stopwords-english.txt");
        } catch(Exception e) { 
            e.printStackTrace(); 
        }
    }

    public static StopWords getInstance() {
        if(instance==null)
            instance=new StopWords();
        return instance;
    }
}

package com.mayalogy.mayu.topsy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Topic implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private List<String> queryPhrases = new ArrayList<String>();
    
    public Topic(String name) {
        this.name=name.toLowerCase();
        addQueryTerm(name);
    }
    
    public void addQueryTerm(String term) {
        String termMod = term.toLowerCase();
        if(!this.queryPhrases.contains(termMod))
            this.queryPhrases.add(termMod);
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<String> getQueryTerms() {
        return this.queryPhrases;
    }
    
    public static String getFileSuitableName(String name) {
        return name.replaceAll(" ", "-").replaceAll("'", "").replaceAll("\"", "");
    }
    
    public boolean isAll() {
        return "all".equals(getName());
    }
}

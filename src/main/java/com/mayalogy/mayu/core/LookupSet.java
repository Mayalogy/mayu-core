package com.mayalogy.mayu.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LookupSet {
    
    private HashSet<String> lookupSet = new HashSet<String>();
    
    public List<String> values() {
        return new ArrayList<String>(lookupSet);
    }
    
    public static Set<String> getFromFile(String fileName, boolean dropCase) throws IOException {
        LookupSet ls = new LookupSet();
        ls.loadFile(fileName);
        return getSet(ls, dropCase);
    }
    
    private static Set<String> getSet(LookupSet ls, boolean dropCase) {
        if(dropCase) {
            Set<String> reS = new HashSet<String>(ls.lookupSet.size());
            for(String s:ls.lookupSet) {
                reS.add(s.toLowerCase());
            }
            return reS;
        }
        return ls.lookupSet;
    }
    
    public static Set<String> getFromStream(BufferedReader inputStream, boolean dropCase) throws IOException {
        LookupSet ls = new LookupSet();
        ls.load(inputStream);
        return getSet(ls, dropCase);
    }

    public void loadFile(String fileName) throws IOException {
        if (fileName == null)
            throw new IOException("Missing filename");

        File file = new File(fileName);
        BufferedReader inputStream = null;
        
        try {
            inputStream = new BufferedReader (new FileReader(file));
        } catch(Exception e) {
            //Look in the jar file before bailing.
            inputStream = new BufferedReader (new InputStreamReader(Class.class.getResourceAsStream(fileName)));
        }
        load(inputStream);
    }
    
    public void load(BufferedReader inputStream) throws IOException {
        this.lookupSet = new HashSet<String>();

        String line;
        while ((line = inputStream.readLine()) != null) {
            if (line.trim().length() > 0) {
                if(line.startsWith("#")) {
                    continue;
                }
                this.lookupSet.add(line.trim());
            } 
        }
        inputStream.close();
    }
    
    public void loadJarResource(String resourceName) throws IOException {       
        InputStream is = Class.class.getResourceAsStream("/"+resourceName);
        if(is==null) { //Used for when jar is running in a servlet.
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            is = classLoader.getResourceAsStream(resourceName);
        }
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(is));
        load(inputStream);
    }
    
    public void add(String term) {
        lookupSet.add(term);
    }

    public boolean contains(String term) {
        if (this.lookupSet == null || term == null) return false;
        return this.lookupSet.contains(term.toLowerCase());
    }
}

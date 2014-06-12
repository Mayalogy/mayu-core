package com.mayalogy.mayu.classifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of labelled classifier data.
 */
public class ClassData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String name;
    private List<Document> documents = new ArrayList<Document>();
    
    public ClassData(String name) {
        this.name=name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void add(Document d) {
        this.documents.add(d);
    }
    
    public List<Document> getDocuments() {
        return this.documents;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("|");
        boolean isFirst=true;
        for(Document d:documents) {
            if(!isFirst)
                sb.append(", ");
            sb.append(d.getName());
            isFirst=false;
        }
        return sb.toString();
    }
}

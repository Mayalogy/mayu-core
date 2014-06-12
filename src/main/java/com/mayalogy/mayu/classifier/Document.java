package com.mayalogy.mayu.classifier;

import java.io.Serializable;

/**
 * Simple representation of a text document to be classified.
 */
public class Document implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private String name = null;
    private String content = null;
    
    public Document(String name, String content) {
        this.content=content;
        this.name=name;
    }
    
    public String getName() {
        return name;
    }
    
    public String getContent() {
        return content;
    }
}

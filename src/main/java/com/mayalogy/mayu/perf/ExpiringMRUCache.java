package com.mayalogy.mayu.perf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


/**
 * This class is open source subject to the Apache 2.0 license available @ http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * Cache that allows aging of elements.
 */
public class ExpiringMRUCache extends MRUCache {
    
    private long ageInMilliseconds=24*60*60*1000;  // 1 day

    /**
     * Constructor.
     *
     * @param maxObjects Max objects in this cache
     * @param ageInMinutes Max time an element remains
     */
    public ExpiringMRUCache(String strName, int maxObjects, int ageInMinutes){
        this(strName, maxObjects);
        ageInMilliseconds=ageInMinutes*60*1000L;
    }
    
    /**
     * Constructor.
     *
     * @param maxObjects Max objects in this cache
     */
    public ExpiringMRUCache(String strName, int maxObjects){
        super(strName, maxObjects);
    }

    /**
     * Returns all objects in the cache.
     */
    public Collection<Object> values() {
        ArrayList<Object> values=new ArrayList<Object>();
        values.addAll(cache.values());
        return values;
    }
    
    /**
     * Returns the specified object.
     */
    public Object get(Object key) {
        ExpirationElement value = (ExpirationElement)super.get(key);
        if (value != null){
            if (value.expired((new Date()).getTime()-ageInMilliseconds)){
                remove(key);
                return null;
            }
            return value.element;
        }
        return value;
    }
    
    /**
     * Adds object to the cache, if full, the LRU object is removed first.
     */
    public void put(Object key, Object value){
        super.put(key, new ExpirationElement(value));
    }
}

class ExpirationElement {

    private long birthInMilliseconds = System.currentTimeMillis();
    Object element;

    public ExpirationElement (Object elem){
        this.element=elem;
    }

    boolean expired(long referenceTime){
        return (referenceTime>birthInMilliseconds);
    }

}

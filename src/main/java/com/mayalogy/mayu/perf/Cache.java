package com.mayalogy.mayu.perf;

/**
 * This class is open source subject to the Apache 2.0 license available @ http://www.apache.org/licenses/LICENSE-2.0.html
 */
public abstract class Cache {
    
    public abstract Object get(Object key);
    public abstract boolean containsKey(  Object key );
    public abstract void put(Object key, Object value);
    public abstract void clear();
    public abstract void remove(Object key);

}

package com.mayalogy.mayu.perf;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * This class is open source subject to the Apache 2.0 license available @ http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * A Simple MRU cache. Synchronization is left to the caller.
 */
public class MRUCache extends Cache {
        
    protected int maxObjectsInCache;
    private CacheNode mruListHead = null;
    protected String name;
    private CacheNode lruListTail = null;       
    protected HashMap<Object, Object> cache;
    protected int hits = 0;
    protected int queries = 0;

    /**
     * Constructs cache.
     */
    public MRUCache(String cacheName, int maxObjectsInCache) {
        this.name = cacheName;
        this.maxObjectsInCache = maxObjectsInCache;
        cache = new HashMap<Object,Object>((int)(1.25*maxObjectsInCache));
    }

    /**
     * Returns object.
     */
    public Object get(Object key) {        
        CacheNode cacheNode = (CacheNode)cache.get(key);
        Object value = null;
        if (cacheNode != null) {
            cacheNode.remove();
            cacheNode.placeBefore(mruListHead);
            hits++;
            value = cacheNode.val;            
        }
        queries++;
        return value;
    }
    
    /**
     * True if key is present.
     */
    public boolean containsKey(  Object key ) {
        return cache.containsKey( key );
    }

    /**
     * Adds object to cache. If full, the LRU
     * object is removed before.
     */
    public void put(Object key, Object value) {
        if ( cache.containsKey(key) ) {
            this.remove(key);
        }

        CacheNode n = new CacheNode(key, value);
        if (lruListTail == null) {
            lruListTail = mruListHead = n;
        } else {
            n.placeBefore(mruListHead);
            mruListHead = n;
        }
        cache.put( key, n );
        if (cache.size() >= maxObjectsInCache) {
            this.remove( lruListTail.key );
        }
    }
    
    public Set<Object> keySet() {
        return cache.keySet();
    }
    
    public void clear() {
        cache.clear();
        mruListHead = null;
        lruListTail = null;
        resetStats();
    }
    
    public void remove(Object key) {
        Object object = cache.get(key);
        if (object!= null) {
            cache.remove(key);
            ((CacheNode)object).remove();
        }
    }
    
    public Collection<Object> values() {
        return cache.values();
    }

    public int getCacheHits() {
        return hits;
    }
    
    public void resetStats() {
        hits    = 0;
        queries = 0;
    }

    private class CacheNode {
        final Object val;
        final Object key;
                
        CacheNode next = null;
        CacheNode prev = null;

        void remove() {
            if (this == lruListTail) lruListTail = prev;
            if (this == mruListHead) mruListHead = next;
            if (next != null) next.prev = prev;
            if (prev != null) prev.next = next;
            next = prev = null;
        }
        
        public CacheNode(Object key, Object val) { 
            this.key = key; 
            this.val = val; 
        }
        
        void placeBefore(CacheNode cacheNode) {
            remove();
            if (cacheNode == null) return;
            next = cacheNode;
            prev = cacheNode.prev;
            cacheNode.prev = this;
            if (prev != null) prev.next = this;
            if ( cacheNode == mruListHead ) mruListHead = this; 
        }
    }
}


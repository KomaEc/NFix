package org.apache.velocity.runtime.resource;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.collections.map.LRUMap;
import org.apache.velocity.runtime.RuntimeServices;

public class ResourceCacheImpl implements ResourceCache {
   protected Map cache = new Hashtable();
   protected RuntimeServices rsvc = null;

   public void initialize(RuntimeServices rs) {
      this.rsvc = rs;
      int maxSize = this.rsvc.getInt("resource.manager.defaultcache.size", 89);
      if (maxSize > 0) {
         Map lruCache = Collections.synchronizedMap(new LRUMap(maxSize));
         lruCache.putAll(this.cache);
         this.cache = lruCache;
      }

      this.rsvc.getLog().debug("ResourceCache: initialized (" + this.getClass() + ')');
   }

   public Resource get(Object key) {
      return (Resource)this.cache.get(key);
   }

   public Resource put(Object key, Resource value) {
      return (Resource)this.cache.put(key, value);
   }

   public Resource remove(Object key) {
      return (Resource)this.cache.remove(key);
   }

   public Iterator enumerateKeys() {
      return this.cache.keySet().iterator();
   }
}

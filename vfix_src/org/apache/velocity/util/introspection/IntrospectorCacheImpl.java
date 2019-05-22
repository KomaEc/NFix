package org.apache.velocity.util.introspection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.velocity.runtime.log.Log;

public final class IntrospectorCacheImpl implements IntrospectorCache {
   private final Log log;
   private final Map classMapCache = new HashMap();
   private final Set classNameCache = new HashSet();
   private final Set listeners = new HashSet();

   public IntrospectorCacheImpl(Log log) {
      this.log = log;
   }

   public synchronized void clear() {
      this.classMapCache.clear();
      this.classNameCache.clear();
      Iterator it = this.listeners.iterator();

      while(it.hasNext()) {
         ((IntrospectorCacheListener)it.next()).triggerClear();
      }

   }

   public synchronized ClassMap get(Class c) {
      if (c == null) {
         throw new IllegalArgumentException("class is null!");
      } else {
         ClassMap classMap = (ClassMap)this.classMapCache.get(c);
         if (classMap == null && this.classNameCache.contains(c.getName())) {
            this.clear();
         }

         Iterator it = this.listeners.iterator();

         while(it.hasNext()) {
            ((IntrospectorCacheListener)it.next()).triggerGet(c, classMap);
         }

         return classMap;
      }
   }

   public synchronized ClassMap put(Class c) {
      ClassMap classMap = new ClassMap(c, this.log);
      this.classMapCache.put(c, classMap);
      this.classNameCache.add(c.getName());
      Iterator it = this.listeners.iterator();

      while(it.hasNext()) {
         ((IntrospectorCacheListener)it.next()).triggerPut(c, classMap);
      }

      return classMap;
   }

   public void addListener(IntrospectorCacheListener listener) {
      this.listeners.add(listener);
   }

   public void removeListener(IntrospectorCacheListener listener) {
      this.listeners.remove(listener);
   }
}

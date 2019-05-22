package soot.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentHashMultiMap<K, V> extends AbstractMultiMap<K, V> {
   private static final long serialVersionUID = -3182515910302586044L;
   Map<K, ConcurrentMap<V, V>> m = new ConcurrentHashMap(0);

   public ConcurrentHashMultiMap() {
   }

   public ConcurrentHashMultiMap(MultiMap<K, V> m) {
      this.putAll(m);
   }

   public int numKeys() {
      return this.m.size();
   }

   public boolean containsKey(Object key) {
      return this.m.containsKey(key);
   }

   public boolean containsValue(V value) {
      Iterator var2 = this.m.values().iterator();

      Map s;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         s = (Map)var2.next();
      } while(!s.containsKey(value));

      return true;
   }

   protected ConcurrentMap<V, V> newSet() {
      return new ConcurrentHashMap();
   }

   private ConcurrentMap<V, V> findSet(K key) {
      ConcurrentMap<V, V> s = (ConcurrentMap)this.m.get(key);
      if (s == null) {
         synchronized(this) {
            s = (ConcurrentMap)this.m.get(key);
            if (s == null) {
               s = this.newSet();
               this.m.put(key, s);
            }
         }
      }

      return s;
   }

   public boolean put(K key, V value) {
      return this.findSet(key).put(value, value) == null;
   }

   public V putIfAbsent(K key, V value) {
      return this.findSet(key).putIfAbsent(value, value);
   }

   public boolean putAll(K key, Set<V> values) {
      if (values != null && !values.isEmpty()) {
         ConcurrentMap<V, V> s = (ConcurrentMap)this.m.get(key);
         if (s == null) {
            synchronized(this) {
               s = (ConcurrentMap)this.m.get(key);
               if (s == null) {
                  ConcurrentMap<V, V> newSet = this.newSet();
                  Iterator var11 = values.iterator();

                  while(var11.hasNext()) {
                     V v = var11.next();
                     newSet.put(v, v);
                  }

                  this.m.put(key, newSet);
                  return true;
               }
            }
         }

         boolean ok = false;
         Iterator var5 = values.iterator();

         while(var5.hasNext()) {
            V v = var5.next();
            if (s.put(v, v) == null) {
               ok = true;
            }
         }

         return ok;
      } else {
         return false;
      }
   }

   public boolean remove(K key, V value) {
      Map<V, V> s = (Map)this.m.get(key);
      if (s == null) {
         return false;
      } else {
         boolean ret = s.remove(value) != null;
         if (s.isEmpty()) {
            this.m.remove(key);
         }

         return ret;
      }
   }

   public boolean remove(K key) {
      return null != this.m.remove(key);
   }

   public boolean removeAll(K key, Set<V> values) {
      Map<V, V> s = (Map)this.m.get(key);
      if (s == null) {
         return false;
      } else {
         boolean ret = false;
         Iterator var5 = values.iterator();

         while(var5.hasNext()) {
            V v = var5.next();
            if (s.remove(v) != null) {
               ret = true;
            }
         }

         if (s.isEmpty()) {
            this.m.remove(key);
         }

         return ret;
      }
   }

   public Set<V> get(K o) {
      Map<V, V> ret = (Map)this.m.get(o);
      return ret == null ? Collections.emptySet() : Collections.unmodifiableSet(ret.keySet());
   }

   public Set<K> keySet() {
      return this.m.keySet();
   }

   public Set<V> values() {
      Set<V> ret = new HashSet(this.m.size());
      Iterator var2 = this.m.values().iterator();

      while(var2.hasNext()) {
         Map<V, V> s = (Map)var2.next();
         ret.addAll(s.keySet());
      }

      return ret;
   }

   public boolean equals(Object o) {
      if (!(o instanceof MultiMap)) {
         return false;
      } else {
         MultiMap<K, V> mm = (MultiMap)o;
         if (!this.keySet().equals(mm.keySet())) {
            return false;
         } else {
            Iterator var3 = this.m.entrySet().iterator();

            Entry e;
            Map s;
            do {
               if (!var3.hasNext()) {
                  return true;
               }

               e = (Entry)var3.next();
               s = (Map)e.getValue();
            } while(s.equals(mm.get(e.getKey())));

            return false;
         }
      }
   }

   public int hashCode() {
      return this.m.hashCode();
   }

   public int size() {
      return this.m.size();
   }

   public void clear() {
      this.m.clear();
   }

   public String toString() {
      return this.m.toString();
   }
}

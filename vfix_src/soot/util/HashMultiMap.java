package soot.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class HashMultiMap<K, V> extends AbstractMultiMap<K, V> {
   private static final long serialVersionUID = -1928446853508616896L;
   private static final float DEFAULT_LOAD_FACTOR = 0.7F;
   protected final Map<K, Set<V>> m;
   protected final float loadFactor;

   protected Map<K, Set<V>> createMap() {
      return this.createMap(0);
   }

   protected Map<K, Set<V>> createMap(int initialSize) {
      return new HashMap(initialSize, this.loadFactor);
   }

   public HashMultiMap() {
      this.loadFactor = 0.7F;
      this.m = this.createMap();
   }

   public HashMultiMap(int initialSize) {
      this.loadFactor = 0.7F;
      this.m = this.createMap(initialSize);
   }

   public HashMultiMap(int initialSize, float loadFactor) {
      this.loadFactor = loadFactor;
      this.m = this.createMap(initialSize);
   }

   public HashMultiMap(MultiMap<K, V> m) {
      this.loadFactor = 0.7F;
      this.m = this.createMap();
      this.putAll(m);
   }

   public HashMultiMap(Map<K, Set<V>> m) {
      this.loadFactor = 0.7F;
      this.m = this.createMap();
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

      Set s;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         s = (Set)var2.next();
      } while(!s.contains(value));

      return true;
   }

   protected Set<V> newSet() {
      return new HashSet(4);
   }

   private Set<V> findSet(K key) {
      Set<V> s = (Set)this.m.get(key);
      if (s == null) {
         s = this.newSet();
         this.m.put(key, s);
      }

      return s;
   }

   public boolean put(K key, V value) {
      return this.findSet(key).add(value);
   }

   public boolean putAll(K key, Set<V> values) {
      return values.isEmpty() ? false : this.findSet(key).addAll(values);
   }

   public boolean remove(K key, V value) {
      Set<V> s = (Set)this.m.get(key);
      if (s == null) {
         return false;
      } else {
         boolean ret = s.remove(value);
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
      Set<V> s = (Set)this.m.get(key);
      if (s == null) {
         return false;
      } else {
         boolean ret = s.removeAll(values);
         if (s.isEmpty()) {
            this.m.remove(key);
         }

         return ret;
      }
   }

   public Set<V> get(K o) {
      Set<V> ret = (Set)this.m.get(o);
      return ret == null ? Collections.emptySet() : ret;
   }

   public Set<K> keySet() {
      return this.m.keySet();
   }

   public Set<V> values() {
      Set<V> ret = new HashSet(this.m.size());
      Iterator var2 = this.m.values().iterator();

      while(var2.hasNext()) {
         Set<V> s = (Set)var2.next();
         ret.addAll(s);
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
            Iterator it = this.m.entrySet().iterator();

            Entry e;
            Set s;
            do {
               if (!it.hasNext()) {
                  return true;
               }

               e = (Entry)it.next();
               s = (Set)e.getValue();
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
}

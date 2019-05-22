package soot.jimple.spark.ondemand.genericutil;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

abstract class AbstractMultiMap<K, V> implements MultiMap<K, V> {
   protected final Map<K, Set<V>> map = new HashMap();
   protected final boolean create;

   protected AbstractMultiMap(boolean create) {
      this.create = create;
   }

   protected abstract Set<V> createSet();

   protected Set<V> emptySet() {
      return Collections.emptySet();
   }

   public Set<V> get(K key) {
      Set<V> ret = (Set)this.map.get(key);
      if (ret == null) {
         if (this.create) {
            ret = this.createSet();
            this.map.put(key, ret);
         } else {
            ret = this.emptySet();
         }
      }

      return ret;
   }

   public boolean put(K key, V val) {
      Set<V> vals = (Set)this.map.get(key);
      if (vals == null) {
         vals = this.createSet();
         this.map.put(key, vals);
      }

      return vals.add(val);
   }

   public boolean remove(K key, V val) {
      Set<V> elems = (Set)this.map.get(key);
      if (elems == null) {
         return false;
      } else {
         boolean ret = elems.remove(val);
         if (elems.isEmpty()) {
            this.map.remove(key);
         }

         return ret;
      }
   }

   public Set<V> removeAll(K key) {
      return (Set)this.map.remove(key);
   }

   public Set<K> keySet() {
      return this.map.keySet();
   }

   public boolean containsKey(K key) {
      return this.map.containsKey(key);
   }

   public int size() {
      int ret = 0;

      Object key;
      for(Iterator var2 = this.keySet().iterator(); var2.hasNext(); ret += this.get(key).size()) {
         key = var2.next();
      }

      return ret;
   }

   public String toString() {
      return this.map.toString();
   }

   public boolean putAll(K key, Collection<? extends V> vals) {
      Set<V> edges = (Set)this.map.get(key);
      if (edges == null) {
         edges = this.createSet();
         this.map.put(key, edges);
      }

      return edges.addAll(vals);
   }

   public void clear() {
      this.map.clear();
   }

   public boolean isEmpty() {
      return this.map.isEmpty();
   }
}

package soot.util;

import heros.solver.Pair;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMultiMap<K, V> implements MultiMap<K, V>, Serializable {
   private static final long serialVersionUID = 4558567794548019671L;

   public boolean putAll(MultiMap<K, V> m) {
      boolean hasNew = false;
      Iterator var3 = m.keySet().iterator();

      while(var3.hasNext()) {
         K key = var3.next();
         if (this.putAll(key, m.get(key))) {
            hasNew = true;
         }
      }

      return hasNew;
   }

   public boolean putAll(Map<K, Set<V>> m) {
      boolean hasNew = false;
      Iterator var3 = m.keySet().iterator();

      while(var3.hasNext()) {
         K key = var3.next();
         if (this.putAll(key, (Set)m.get(key))) {
            hasNew = true;
         }
      }

      return hasNew;
   }

   public boolean isEmpty() {
      return this.numKeys() == 0;
   }

   public boolean contains(K key, V value) {
      Set<V> set = this.get(key);
      return set == null ? false : set.contains(value);
   }

   public Iterator<Pair<K, V>> iterator() {
      return new AbstractMultiMap.EntryIterator();
   }

   private class EntryIterator implements Iterator<Pair<K, V>> {
      Iterator<K> keyIterator;
      Iterator<V> valueIterator;
      K currentKey;

      private EntryIterator() {
         this.keyIterator = AbstractMultiMap.this.keySet().iterator();
         this.valueIterator = null;
         this.currentKey = null;
      }

      public boolean hasNext() {
         if (this.valueIterator != null && this.valueIterator.hasNext()) {
            return true;
         } else {
            this.valueIterator = null;
            this.currentKey = null;
            return this.keyIterator.hasNext();
         }
      }

      public Pair<K, V> next() {
         if (this.valueIterator == null) {
            this.currentKey = this.keyIterator.next();
            this.valueIterator = AbstractMultiMap.this.get(this.currentKey).iterator();
         }

         return new Pair(this.currentKey, this.valueIterator.next());
      }

      public void remove() {
         if (this.valueIterator != null) {
            this.valueIterator.remove();
            if (AbstractMultiMap.this.get(this.currentKey).isEmpty()) {
               this.keyIterator.remove();
               this.valueIterator = null;
               this.currentKey = null;
            }

         }
      }

      // $FF: synthetic method
      EntryIterator(Object x1) {
         this();
      }
   }
}

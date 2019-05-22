package soot.util;

import java.util.HashMap;
import java.util.Set;

public class DeterministicHashMap<K, V> extends HashMap<K, V> {
   Set<K> keys = new TrustingMonotonicArraySet();

   public DeterministicHashMap(int initialCapacity) {
      super(initialCapacity);
   }

   public DeterministicHashMap(int initialCapacity, float loadFactor) {
      super(initialCapacity, loadFactor);
   }

   public V put(K key, V value) {
      if (!this.containsKey(key)) {
         this.keys.add(key);
      }

      return super.put(key, value);
   }

   public V remove(Object obj) {
      throw new UnsupportedOperationException();
   }

   public Set<K> keySet() {
      return this.keys;
   }
}

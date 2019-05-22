package org.jboss.util.collection;

import java.lang.ref.ReferenceQueue;
import java.util.Map;

public class SoftValueHashMap<K, V> extends ReferenceValueHashMap<K, V> {
   public SoftValueHashMap(int initialCapacity, float loadFactor) {
      super(initialCapacity, loadFactor);
   }

   public SoftValueHashMap(int initialCapacity) {
      super(initialCapacity);
   }

   public SoftValueHashMap() {
   }

   public SoftValueHashMap(Map<K, V> t) {
      super(t);
   }

   protected ValueRef<K, V> create(K key, V value, ReferenceQueue<V> q) {
      return SoftValueRef.create(key, value, q);
   }
}

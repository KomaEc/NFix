package org.jboss.util.collection;

import java.lang.ref.ReferenceQueue;
import java.util.Map;

public class WeakValueHashMap<K, V> extends ReferenceValueHashMap<K, V> {
   public WeakValueHashMap(int initialCapacity, float loadFactor) {
      super(initialCapacity, loadFactor);
   }

   public WeakValueHashMap(int initialCapacity) {
      super(initialCapacity);
   }

   public WeakValueHashMap() {
   }

   public WeakValueHashMap(Map<K, V> t) {
      super(t);
   }

   protected ValueRef<K, V> create(K key, V value, ReferenceQueue<V> q) {
      return WeakValueRef.create(key, value, q);
   }
}

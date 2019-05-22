package org.jboss.util.collection;

import java.lang.ref.ReferenceQueue;
import java.util.Comparator;
import java.util.SortedMap;

public class WeakValueTreeMap<K, V> extends ReferenceValueTreeMap<K, V> {
   public WeakValueTreeMap() {
   }

   public WeakValueTreeMap(Comparator<K> kComparator) {
      super(kComparator);
   }

   public WeakValueTreeMap(SortedMap<K, ValueRef<K, V>> sorted) {
      super(sorted);
   }

   protected ValueRef<K, V> create(K key, V value, ReferenceQueue<V> q) {
      return WeakValueRef.create(key, value, q);
   }
}

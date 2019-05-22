package org.jboss.util.collection;

import java.lang.ref.ReferenceQueue;
import java.util.Comparator;
import java.util.SortedMap;

public class SoftValueTreeMap<K, V> extends ReferenceValueTreeMap<K, V> {
   public SoftValueTreeMap() {
   }

   public SoftValueTreeMap(Comparator<K> comparator) {
      super(comparator);
   }

   public SoftValueTreeMap(SortedMap<K, ValueRef<K, V>> sorted) {
      super(sorted);
   }

   protected ValueRef<K, V> create(K key, V value, ReferenceQueue<V> q) {
      return SoftValueRef.create(key, value, q);
   }
}

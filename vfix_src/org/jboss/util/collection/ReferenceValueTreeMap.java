package org.jboss.util.collection;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class ReferenceValueTreeMap<K, V> extends ReferenceValueMap<K, V> {
   protected ReferenceValueTreeMap() {
   }

   protected ReferenceValueTreeMap(Comparator<K> comparator) {
      super(comparator);
   }

   protected ReferenceValueTreeMap(SortedMap<K, ValueRef<K, V>> sorted) {
      super(sorted);
   }

   protected Map<K, ValueRef<K, V>> createMap() {
      return new TreeMap();
   }

   protected Map<K, ValueRef<K, V>> createMap(Comparator<K> comparator) {
      return new TreeMap(comparator);
   }

   protected Map<K, ValueRef<K, V>> createMap(SortedMap<K, ValueRef<K, V>> map) {
      return new TreeMap(map);
   }

   protected Map<K, ValueRef<K, V>> createMap(int initialCapacity) {
      throw new UnsupportedOperationException("Cannot create TreeMap with such parameters.");
   }

   protected Map<K, ValueRef<K, V>> createMap(int initialCapacity, float loadFactor) {
      throw new UnsupportedOperationException("Cannot create TreeMap with such parameters.");
   }
}

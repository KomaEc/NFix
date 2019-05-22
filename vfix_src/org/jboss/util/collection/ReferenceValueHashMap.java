package org.jboss.util.collection;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public abstract class ReferenceValueHashMap<K, V> extends ReferenceValueMap<K, V> {
   protected ReferenceValueHashMap() {
   }

   protected ReferenceValueHashMap(int initialCapacity) {
      super(initialCapacity);
   }

   protected ReferenceValueHashMap(int initialCapacity, float loadFactor) {
      super(initialCapacity, loadFactor);
   }

   protected ReferenceValueHashMap(Map<K, V> t) {
      super(t);
   }

   protected Map<K, ValueRef<K, V>> createMap(int initialCapacity, float loadFactor) {
      return new HashMap(initialCapacity, loadFactor);
   }

   protected Map<K, ValueRef<K, V>> createMap(int initialCapacity) {
      return new HashMap(initialCapacity);
   }

   protected Map<K, ValueRef<K, V>> createMap() {
      return new HashMap();
   }

   protected Map<K, ValueRef<K, V>> createMap(Comparator<K> kComparator) {
      throw new UnsupportedOperationException("Cannot create HashMap with such parameters.");
   }

   protected Map<K, ValueRef<K, V>> createMap(SortedMap<K, ValueRef<K, V>> kValueRefSortedMap) {
      throw new UnsupportedOperationException("Cannot create HashMap with such parameters.");
   }
}

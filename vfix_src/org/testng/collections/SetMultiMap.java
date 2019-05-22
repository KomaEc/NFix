package org.testng.collections;

import java.util.Set;

public class SetMultiMap<K, V> extends MultiMap<K, V, Set<V>> {
   protected Set<V> createValue() {
      return Sets.newHashSet();
   }
}

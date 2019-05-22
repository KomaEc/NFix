package org.jboss.util.collection;

import java.util.concurrent.ConcurrentMap;

public interface ConcurrentNavigableMap<K, V> extends ConcurrentMap<K, V>, NavigableMap<K, V> {
   ConcurrentNavigableMap<K, V> subMap(K var1, K var2);

   ConcurrentNavigableMap<K, V> headMap(K var1);

   ConcurrentNavigableMap<K, V> tailMap(K var1);
}

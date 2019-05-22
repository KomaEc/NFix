package org.jboss.util.collection;

import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;

public interface NavigableMap<K, V> extends SortedMap<K, V> {
   Entry<K, V> ceilingEntry(K var1);

   K ceilingKey(K var1);

   Entry<K, V> lowerEntry(K var1);

   K lowerKey(K var1);

   Entry<K, V> floorEntry(K var1);

   K floorKey(K var1);

   Entry<K, V> higherEntry(K var1);

   K higherKey(K var1);

   Entry<K, V> firstEntry();

   Entry<K, V> lastEntry();

   Entry<K, V> pollFirstEntry();

   Entry<K, V> pollLastEntry();

   Set<K> descendingKeySet();

   Set<Entry<K, V>> descendingEntrySet();

   NavigableMap<K, V> subMap(K var1, K var2);

   NavigableMap<K, V> headMap(K var1);

   NavigableMap<K, V> tailMap(K var1);
}

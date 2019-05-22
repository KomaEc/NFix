package edu.emory.mathcs.backport.java.util;

import java.util.SortedMap;
import java.util.Map.Entry;

public interface NavigableMap extends SortedMap {
   Entry lowerEntry(Object var1);

   Object lowerKey(Object var1);

   Entry floorEntry(Object var1);

   Object floorKey(Object var1);

   Entry ceilingEntry(Object var1);

   Object ceilingKey(Object var1);

   Entry higherEntry(Object var1);

   Object higherKey(Object var1);

   Entry firstEntry();

   Entry lastEntry();

   Entry pollFirstEntry();

   Entry pollLastEntry();

   NavigableMap descendingMap();

   NavigableSet navigableKeySet();

   NavigableSet descendingKeySet();

   NavigableMap subMap(Object var1, boolean var2, Object var3, boolean var4);

   NavigableMap headMap(Object var1, boolean var2);

   NavigableMap tailMap(Object var1, boolean var2);

   SortedMap subMap(Object var1, Object var2);

   SortedMap headMap(Object var1);

   SortedMap tailMap(Object var1);
}

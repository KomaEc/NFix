package edu.emory.mathcs.backport.java.util.concurrent;

import edu.emory.mathcs.backport.java.util.NavigableMap;
import edu.emory.mathcs.backport.java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;

public interface ConcurrentNavigableMap extends ConcurrentMap, NavigableMap {
   NavigableMap subMap(Object var1, boolean var2, Object var3, boolean var4);

   NavigableMap headMap(Object var1, boolean var2);

   NavigableMap tailMap(Object var1, boolean var2);

   SortedMap subMap(Object var1, Object var2);

   SortedMap headMap(Object var1);

   SortedMap tailMap(Object var1);

   NavigableMap descendingMap();

   NavigableSet navigableKeySet();

   Set keySet();

   NavigableSet descendingKeySet();
}

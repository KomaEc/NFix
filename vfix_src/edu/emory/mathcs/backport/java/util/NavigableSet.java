package edu.emory.mathcs.backport.java.util;

import java.util.Iterator;
import java.util.SortedSet;

public interface NavigableSet extends SortedSet {
   Object lower(Object var1);

   Object floor(Object var1);

   Object ceiling(Object var1);

   Object higher(Object var1);

   Object pollFirst();

   Object pollLast();

   Iterator iterator();

   NavigableSet descendingSet();

   Iterator descendingIterator();

   NavigableSet subSet(Object var1, boolean var2, Object var3, boolean var4);

   NavigableSet headSet(Object var1, boolean var2);

   NavigableSet tailSet(Object var1, boolean var2);

   SortedSet subSet(Object var1, Object var2);

   SortedSet headSet(Object var1);

   SortedSet tailSet(Object var1);
}

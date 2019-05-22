package edu.emory.mathcs.backport.java.util.concurrent;

import java.util.Map;

public interface ConcurrentMap extends Map {
   Object putIfAbsent(Object var1, Object var2);

   boolean remove(Object var1, Object var2);

   boolean replace(Object var1, Object var2, Object var3);

   Object replace(Object var1, Object var2);
}

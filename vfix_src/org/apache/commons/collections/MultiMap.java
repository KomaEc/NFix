package org.apache.commons.collections;

import java.util.Collection;
import java.util.Map;

public interface MultiMap extends Map {
   Object remove(Object var1, Object var2);

   int size();

   Object get(Object var1);

   boolean containsValue(Object var1);

   Object put(Object var1, Object var2);

   Object remove(Object var1);

   Collection values();
}

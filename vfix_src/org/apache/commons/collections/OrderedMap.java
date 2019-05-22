package org.apache.commons.collections;

public interface OrderedMap extends IterableMap {
   OrderedMapIterator orderedMapIterator();

   Object firstKey();

   Object lastKey();

   Object nextKey(Object var1);

   Object previousKey(Object var1);
}

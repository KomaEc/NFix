package org.apache.commons.collections;

public interface BidiMap extends IterableMap {
   MapIterator mapIterator();

   Object put(Object var1, Object var2);

   Object getKey(Object var1);

   Object removeValue(Object var1);

   BidiMap inverseBidiMap();
}

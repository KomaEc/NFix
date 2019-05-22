package com.google.common.collect;

import java.util.AbstractSet;

abstract class MapMakerInternalMap$SafeToArraySet<E> extends AbstractSet<E> {
   private MapMakerInternalMap$SafeToArraySet() {
   }

   public Object[] toArray() {
      return MapMakerInternalMap.access$900(this).toArray();
   }

   public <T> T[] toArray(T[] a) {
      return MapMakerInternalMap.access$900(this).toArray(a);
   }

   // $FF: synthetic method
   MapMakerInternalMap$SafeToArraySet(Object x0) {
      this();
   }
}

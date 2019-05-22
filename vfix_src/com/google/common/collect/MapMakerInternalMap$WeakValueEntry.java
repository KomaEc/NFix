package com.google.common.collect;

interface MapMakerInternalMap$WeakValueEntry<K, V, E extends MapMakerInternalMap$InternalEntry<K, V, E>> extends MapMakerInternalMap$InternalEntry<K, V, E> {
   MapMakerInternalMap.WeakValueReference<K, V, E> getValueReference();

   void clearValue();
}

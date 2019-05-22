package com.google.common.collect;

interface MapMakerInternalMap$InternalEntry<K, V, E extends MapMakerInternalMap$InternalEntry<K, V, E>> {
   E getNext();

   int getHash();

   K getKey();

   V getValue();
}

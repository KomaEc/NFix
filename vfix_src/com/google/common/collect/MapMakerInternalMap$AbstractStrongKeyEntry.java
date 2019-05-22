package com.google.common.collect;

import javax.annotation.Nullable;

abstract class MapMakerInternalMap$AbstractStrongKeyEntry<K, V, E extends MapMakerInternalMap$InternalEntry<K, V, E>> implements MapMakerInternalMap$InternalEntry<K, V, E> {
   final K key;
   final int hash;
   final E next;

   MapMakerInternalMap$AbstractStrongKeyEntry(K key, int hash, @Nullable E next) {
      this.key = key;
      this.hash = hash;
      this.next = next;
   }

   public K getKey() {
      return this.key;
   }

   public int getHash() {
      return this.hash;
   }

   public E getNext() {
      return this.next;
   }
}

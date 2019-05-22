package com.google.common.collect;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import javax.annotation.Nullable;

abstract class MapMakerInternalMap$AbstractWeakKeyEntry<K, V, E extends MapMakerInternalMap$InternalEntry<K, V, E>> extends WeakReference<K> implements MapMakerInternalMap$InternalEntry<K, V, E> {
   final int hash;
   final E next;

   MapMakerInternalMap$AbstractWeakKeyEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable E next) {
      super(key, queue);
      this.hash = hash;
      this.next = next;
   }

   public K getKey() {
      return this.get();
   }

   public int getHash() {
      return this.hash;
   }

   public E getNext() {
      return this.next;
   }
}

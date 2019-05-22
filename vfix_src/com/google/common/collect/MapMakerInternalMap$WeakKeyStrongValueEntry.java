package com.google.common.collect;

import java.lang.ref.ReferenceQueue;
import javax.annotation.Nullable;

final class MapMakerInternalMap$WeakKeyStrongValueEntry<K, V> extends MapMakerInternalMap$AbstractWeakKeyEntry<K, V, MapMakerInternalMap$WeakKeyStrongValueEntry<K, V>> implements MapMakerInternalMap$StrongValueEntry<K, V, MapMakerInternalMap$WeakKeyStrongValueEntry<K, V>> {
   @Nullable
   private volatile V value = null;

   MapMakerInternalMap$WeakKeyStrongValueEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable MapMakerInternalMap$WeakKeyStrongValueEntry<K, V> next) {
      super(queue, key, hash, next);
   }

   @Nullable
   public V getValue() {
      return this.value;
   }

   void setValue(V value) {
      this.value = value;
   }

   MapMakerInternalMap$WeakKeyStrongValueEntry<K, V> copy(ReferenceQueue<K> queueForKeys, MapMakerInternalMap$WeakKeyStrongValueEntry<K, V> newNext) {
      MapMakerInternalMap$WeakKeyStrongValueEntry<K, V> newEntry = new MapMakerInternalMap$WeakKeyStrongValueEntry(queueForKeys, this.getKey(), this.hash, newNext);
      newEntry.setValue(this.value);
      return newEntry;
   }
}

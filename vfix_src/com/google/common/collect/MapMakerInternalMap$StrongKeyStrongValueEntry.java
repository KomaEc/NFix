package com.google.common.collect;

import javax.annotation.Nullable;

final class MapMakerInternalMap$StrongKeyStrongValueEntry<K, V> extends MapMakerInternalMap$AbstractStrongKeyEntry<K, V, MapMakerInternalMap$StrongKeyStrongValueEntry<K, V>> implements MapMakerInternalMap$StrongValueEntry<K, V, MapMakerInternalMap$StrongKeyStrongValueEntry<K, V>> {
   @Nullable
   private volatile V value = null;

   MapMakerInternalMap$StrongKeyStrongValueEntry(K key, int hash, @Nullable MapMakerInternalMap$StrongKeyStrongValueEntry<K, V> next) {
      super(key, hash, next);
   }

   @Nullable
   public V getValue() {
      return this.value;
   }

   void setValue(V value) {
      this.value = value;
   }

   MapMakerInternalMap$StrongKeyStrongValueEntry<K, V> copy(MapMakerInternalMap$StrongKeyStrongValueEntry<K, V> newNext) {
      MapMakerInternalMap$StrongKeyStrongValueEntry<K, V> newEntry = new MapMakerInternalMap$StrongKeyStrongValueEntry(this.key, this.hash, newNext);
      newEntry.value = this.value;
      return newEntry;
   }
}

package com.google.common.collect;

import java.lang.ref.ReferenceQueue;
import javax.annotation.Nullable;

final class MapMakerInternalMap$WeakKeyDummyValueEntry<K> extends MapMakerInternalMap$AbstractWeakKeyEntry<K, MapMaker$Dummy, MapMakerInternalMap$WeakKeyDummyValueEntry<K>> implements MapMakerInternalMap$StrongValueEntry<K, MapMaker$Dummy, MapMakerInternalMap$WeakKeyDummyValueEntry<K>> {
   MapMakerInternalMap$WeakKeyDummyValueEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable MapMakerInternalMap$WeakKeyDummyValueEntry<K> next) {
      super(queue, key, hash, next);
   }

   public MapMaker$Dummy getValue() {
      return MapMaker$Dummy.VALUE;
   }

   void setValue(MapMaker$Dummy value) {
   }

   MapMakerInternalMap$WeakKeyDummyValueEntry<K> copy(ReferenceQueue<K> queueForKeys, MapMakerInternalMap$WeakKeyDummyValueEntry<K> newNext) {
      return new MapMakerInternalMap$WeakKeyDummyValueEntry(queueForKeys, this.getKey(), this.hash, newNext);
   }
}

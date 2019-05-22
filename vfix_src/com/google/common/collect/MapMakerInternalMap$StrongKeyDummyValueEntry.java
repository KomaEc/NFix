package com.google.common.collect;

import javax.annotation.Nullable;

final class MapMakerInternalMap$StrongKeyDummyValueEntry<K> extends MapMakerInternalMap$AbstractStrongKeyEntry<K, MapMaker$Dummy, MapMakerInternalMap$StrongKeyDummyValueEntry<K>> implements MapMakerInternalMap$StrongValueEntry<K, MapMaker$Dummy, MapMakerInternalMap$StrongKeyDummyValueEntry<K>> {
   MapMakerInternalMap$StrongKeyDummyValueEntry(K key, int hash, @Nullable MapMakerInternalMap$StrongKeyDummyValueEntry<K> next) {
      super(key, hash, next);
   }

   public MapMaker$Dummy getValue() {
      return MapMaker$Dummy.VALUE;
   }

   void setValue(MapMaker$Dummy value) {
   }

   MapMakerInternalMap$StrongKeyDummyValueEntry<K> copy(MapMakerInternalMap$StrongKeyDummyValueEntry<K> newNext) {
      return new MapMakerInternalMap$StrongKeyDummyValueEntry(this.key, this.hash, newNext);
   }
}

package com.google.common.collect;

import javax.annotation.Nullable;

final class MapMakerInternalMap$StrongKeyDummyValueEntry$Helper<K> implements MapMakerInternalMap$InternalEntryHelper<K, MapMaker$Dummy, MapMakerInternalMap$StrongKeyDummyValueEntry<K>, MapMakerInternalMap$StrongKeyDummyValueSegment<K>> {
   private static final MapMakerInternalMap$StrongKeyDummyValueEntry$Helper<?> INSTANCE = new MapMakerInternalMap$StrongKeyDummyValueEntry$Helper();

   static <K> MapMakerInternalMap$StrongKeyDummyValueEntry$Helper<K> instance() {
      return INSTANCE;
   }

   public MapMakerInternalMap.Strength keyStrength() {
      return MapMakerInternalMap.Strength.STRONG;
   }

   public MapMakerInternalMap.Strength valueStrength() {
      return MapMakerInternalMap.Strength.STRONG;
   }

   public MapMakerInternalMap$StrongKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker$Dummy, MapMakerInternalMap$StrongKeyDummyValueEntry<K>, MapMakerInternalMap$StrongKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
      return new MapMakerInternalMap$StrongKeyDummyValueSegment(map, initialCapacity, maxSegmentSize);
   }

   public MapMakerInternalMap$StrongKeyDummyValueEntry<K> copy(MapMakerInternalMap$StrongKeyDummyValueSegment<K> segment, MapMakerInternalMap$StrongKeyDummyValueEntry<K> entry, @Nullable MapMakerInternalMap$StrongKeyDummyValueEntry<K> newNext) {
      return entry.copy(newNext);
   }

   public void setValue(MapMakerInternalMap$StrongKeyDummyValueSegment<K> segment, MapMakerInternalMap$StrongKeyDummyValueEntry<K> entry, MapMaker$Dummy value) {
   }

   public MapMakerInternalMap$StrongKeyDummyValueEntry<K> newEntry(MapMakerInternalMap$StrongKeyDummyValueSegment<K> segment, K key, int hash, @Nullable MapMakerInternalMap$StrongKeyDummyValueEntry<K> next) {
      return new MapMakerInternalMap$StrongKeyDummyValueEntry(key, hash, next);
   }
}

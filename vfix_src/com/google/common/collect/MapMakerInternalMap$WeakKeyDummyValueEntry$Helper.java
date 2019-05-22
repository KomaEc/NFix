package com.google.common.collect;

import javax.annotation.Nullable;

final class MapMakerInternalMap$WeakKeyDummyValueEntry$Helper<K> implements MapMakerInternalMap$InternalEntryHelper<K, MapMaker$Dummy, MapMakerInternalMap$WeakKeyDummyValueEntry<K>, MapMakerInternalMap$WeakKeyDummyValueSegment<K>> {
   private static final MapMakerInternalMap$WeakKeyDummyValueEntry$Helper<?> INSTANCE = new MapMakerInternalMap$WeakKeyDummyValueEntry$Helper();

   static <K> MapMakerInternalMap$WeakKeyDummyValueEntry$Helper<K> instance() {
      return INSTANCE;
   }

   public MapMakerInternalMap.Strength keyStrength() {
      return MapMakerInternalMap.Strength.WEAK;
   }

   public MapMakerInternalMap.Strength valueStrength() {
      return MapMakerInternalMap.Strength.STRONG;
   }

   public MapMakerInternalMap$WeakKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker$Dummy, MapMakerInternalMap$WeakKeyDummyValueEntry<K>, MapMakerInternalMap$WeakKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
      return new MapMakerInternalMap$WeakKeyDummyValueSegment(map, initialCapacity, maxSegmentSize);
   }

   public MapMakerInternalMap$WeakKeyDummyValueEntry<K> copy(MapMakerInternalMap$WeakKeyDummyValueSegment<K> segment, MapMakerInternalMap$WeakKeyDummyValueEntry<K> entry, @Nullable MapMakerInternalMap$WeakKeyDummyValueEntry<K> newNext) {
      return entry.getKey() == null ? null : entry.copy(MapMakerInternalMap$WeakKeyDummyValueSegment.access$200(segment), newNext);
   }

   public void setValue(MapMakerInternalMap$WeakKeyDummyValueSegment<K> segment, MapMakerInternalMap$WeakKeyDummyValueEntry<K> entry, MapMaker$Dummy value) {
   }

   public MapMakerInternalMap$WeakKeyDummyValueEntry<K> newEntry(MapMakerInternalMap$WeakKeyDummyValueSegment<K> segment, K key, int hash, @Nullable MapMakerInternalMap$WeakKeyDummyValueEntry<K> next) {
      return new MapMakerInternalMap$WeakKeyDummyValueEntry(MapMakerInternalMap$WeakKeyDummyValueSegment.access$200(segment), key, hash, next);
   }
}

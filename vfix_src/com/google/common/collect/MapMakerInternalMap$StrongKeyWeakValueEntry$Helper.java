package com.google.common.collect;

import javax.annotation.Nullable;

final class MapMakerInternalMap$StrongKeyWeakValueEntry$Helper<K, V> implements MapMakerInternalMap$InternalEntryHelper<K, V, MapMakerInternalMap$StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap$StrongKeyWeakValueSegment<K, V>> {
   private static final MapMakerInternalMap$StrongKeyWeakValueEntry$Helper<?, ?> INSTANCE = new MapMakerInternalMap$StrongKeyWeakValueEntry$Helper();

   static <K, V> MapMakerInternalMap$StrongKeyWeakValueEntry$Helper<K, V> instance() {
      return INSTANCE;
   }

   public MapMakerInternalMap.Strength keyStrength() {
      return MapMakerInternalMap.Strength.STRONG;
   }

   public MapMakerInternalMap.Strength valueStrength() {
      return MapMakerInternalMap.Strength.WEAK;
   }

   public MapMakerInternalMap$StrongKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap$StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap$StrongKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
      return new MapMakerInternalMap$StrongKeyWeakValueSegment(map, initialCapacity, maxSegmentSize);
   }

   public MapMakerInternalMap$StrongKeyWeakValueEntry<K, V> copy(MapMakerInternalMap$StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap$StrongKeyWeakValueEntry<K, V> entry, @Nullable MapMakerInternalMap$StrongKeyWeakValueEntry<K, V> newNext) {
      return MapMakerInternalMap.Segment.isCollected(entry) ? null : entry.copy(MapMakerInternalMap$StrongKeyWeakValueSegment.access$100(segment), newNext);
   }

   public void setValue(MapMakerInternalMap$StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap$StrongKeyWeakValueEntry<K, V> entry, V value) {
      entry.setValue(value, MapMakerInternalMap$StrongKeyWeakValueSegment.access$100(segment));
   }

   public MapMakerInternalMap$StrongKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap$StrongKeyWeakValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap$StrongKeyWeakValueEntry<K, V> next) {
      return new MapMakerInternalMap$StrongKeyWeakValueEntry(key, hash, next);
   }
}

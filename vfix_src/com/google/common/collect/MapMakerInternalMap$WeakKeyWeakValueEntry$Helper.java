package com.google.common.collect;

import javax.annotation.Nullable;

final class MapMakerInternalMap$WeakKeyWeakValueEntry$Helper<K, V> implements MapMakerInternalMap$InternalEntryHelper<K, V, MapMakerInternalMap$WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap$WeakKeyWeakValueSegment<K, V>> {
   private static final MapMakerInternalMap$WeakKeyWeakValueEntry$Helper<?, ?> INSTANCE = new MapMakerInternalMap$WeakKeyWeakValueEntry$Helper();

   static <K, V> MapMakerInternalMap$WeakKeyWeakValueEntry$Helper<K, V> instance() {
      return INSTANCE;
   }

   public MapMakerInternalMap.Strength keyStrength() {
      return MapMakerInternalMap.Strength.WEAK;
   }

   public MapMakerInternalMap.Strength valueStrength() {
      return MapMakerInternalMap.Strength.WEAK;
   }

   public MapMakerInternalMap$WeakKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap$WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap$WeakKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
      return new MapMakerInternalMap$WeakKeyWeakValueSegment(map, initialCapacity, maxSegmentSize);
   }

   public MapMakerInternalMap$WeakKeyWeakValueEntry<K, V> copy(MapMakerInternalMap$WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap$WeakKeyWeakValueEntry<K, V> entry, @Nullable MapMakerInternalMap$WeakKeyWeakValueEntry<K, V> newNext) {
      if (entry.getKey() == null) {
         return null;
      } else {
         return MapMakerInternalMap.Segment.isCollected(entry) ? null : entry.copy(MapMakerInternalMap$WeakKeyWeakValueSegment.access$400(segment), MapMakerInternalMap$WeakKeyWeakValueSegment.access$500(segment), newNext);
      }
   }

   public void setValue(MapMakerInternalMap$WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap$WeakKeyWeakValueEntry<K, V> entry, V value) {
      entry.setValue(value, MapMakerInternalMap$WeakKeyWeakValueSegment.access$500(segment));
   }

   public MapMakerInternalMap$WeakKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap$WeakKeyWeakValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap$WeakKeyWeakValueEntry<K, V> next) {
      return new MapMakerInternalMap$WeakKeyWeakValueEntry(MapMakerInternalMap$WeakKeyWeakValueSegment.access$400(segment), key, hash, next);
   }
}

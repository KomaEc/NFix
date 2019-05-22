package com.google.common.collect;

import javax.annotation.Nullable;

final class MapMakerInternalMap$WeakKeyStrongValueEntry$Helper<K, V> implements MapMakerInternalMap$InternalEntryHelper<K, V, MapMakerInternalMap$WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap$WeakKeyStrongValueSegment<K, V>> {
   private static final MapMakerInternalMap$WeakKeyStrongValueEntry$Helper<?, ?> INSTANCE = new MapMakerInternalMap$WeakKeyStrongValueEntry$Helper();

   static <K, V> MapMakerInternalMap$WeakKeyStrongValueEntry$Helper<K, V> instance() {
      return INSTANCE;
   }

   public MapMakerInternalMap.Strength keyStrength() {
      return MapMakerInternalMap.Strength.WEAK;
   }

   public MapMakerInternalMap.Strength valueStrength() {
      return MapMakerInternalMap.Strength.STRONG;
   }

   public MapMakerInternalMap$WeakKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap$WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap$WeakKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
      return new MapMakerInternalMap$WeakKeyStrongValueSegment(map, initialCapacity, maxSegmentSize);
   }

   public MapMakerInternalMap$WeakKeyStrongValueEntry<K, V> copy(MapMakerInternalMap$WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap$WeakKeyStrongValueEntry<K, V> entry, @Nullable MapMakerInternalMap$WeakKeyStrongValueEntry<K, V> newNext) {
      return entry.getKey() == null ? null : entry.copy(MapMakerInternalMap$WeakKeyStrongValueSegment.access$300(segment), newNext);
   }

   public void setValue(MapMakerInternalMap$WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap$WeakKeyStrongValueEntry<K, V> entry, V value) {
      entry.setValue(value);
   }

   public MapMakerInternalMap$WeakKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap$WeakKeyStrongValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap$WeakKeyStrongValueEntry<K, V> next) {
      return new MapMakerInternalMap$WeakKeyStrongValueEntry(MapMakerInternalMap$WeakKeyStrongValueSegment.access$300(segment), key, hash, next);
   }
}

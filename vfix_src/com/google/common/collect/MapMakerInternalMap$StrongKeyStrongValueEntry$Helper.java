package com.google.common.collect;

import javax.annotation.Nullable;

final class MapMakerInternalMap$StrongKeyStrongValueEntry$Helper<K, V> implements MapMakerInternalMap$InternalEntryHelper<K, V, MapMakerInternalMap$StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap$StrongKeyStrongValueSegment<K, V>> {
   private static final MapMakerInternalMap$StrongKeyStrongValueEntry$Helper<?, ?> INSTANCE = new MapMakerInternalMap$StrongKeyStrongValueEntry$Helper();

   static <K, V> MapMakerInternalMap$StrongKeyStrongValueEntry$Helper<K, V> instance() {
      return INSTANCE;
   }

   public MapMakerInternalMap.Strength keyStrength() {
      return MapMakerInternalMap.Strength.STRONG;
   }

   public MapMakerInternalMap.Strength valueStrength() {
      return MapMakerInternalMap.Strength.STRONG;
   }

   public MapMakerInternalMap$StrongKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap$StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap$StrongKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
      return new MapMakerInternalMap$StrongKeyStrongValueSegment(map, initialCapacity, maxSegmentSize);
   }

   public MapMakerInternalMap$StrongKeyStrongValueEntry<K, V> copy(MapMakerInternalMap$StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap$StrongKeyStrongValueEntry<K, V> entry, @Nullable MapMakerInternalMap$StrongKeyStrongValueEntry<K, V> newNext) {
      return entry.copy(newNext);
   }

   public void setValue(MapMakerInternalMap$StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap$StrongKeyStrongValueEntry<K, V> entry, V value) {
      entry.setValue(value);
   }

   public MapMakerInternalMap$StrongKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap$StrongKeyStrongValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap$StrongKeyStrongValueEntry<K, V> next) {
      return new MapMakerInternalMap$StrongKeyStrongValueEntry(key, hash, next);
   }
}

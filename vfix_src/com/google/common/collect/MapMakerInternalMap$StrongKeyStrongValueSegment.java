package com.google.common.collect;

final class MapMakerInternalMap$StrongKeyStrongValueSegment<K, V> extends MapMakerInternalMap.Segment<K, V, MapMakerInternalMap$StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap$StrongKeyStrongValueSegment<K, V>> {
   MapMakerInternalMap$StrongKeyStrongValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap$StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap$StrongKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
      super(map, initialCapacity, maxSegmentSize);
   }

   MapMakerInternalMap$StrongKeyStrongValueSegment<K, V> self() {
      return this;
   }

   public MapMakerInternalMap$StrongKeyStrongValueEntry<K, V> castForTesting(MapMakerInternalMap$InternalEntry<K, V, ?> entry) {
      return (MapMakerInternalMap$StrongKeyStrongValueEntry)entry;
   }
}

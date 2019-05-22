package com.google.common.collect;

final class MapMakerInternalMap$StrongKeyDummyValueSegment<K> extends MapMakerInternalMap.Segment<K, MapMaker$Dummy, MapMakerInternalMap$StrongKeyDummyValueEntry<K>, MapMakerInternalMap$StrongKeyDummyValueSegment<K>> {
   MapMakerInternalMap$StrongKeyDummyValueSegment(MapMakerInternalMap<K, MapMaker$Dummy, MapMakerInternalMap$StrongKeyDummyValueEntry<K>, MapMakerInternalMap$StrongKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
      super(map, initialCapacity, maxSegmentSize);
   }

   MapMakerInternalMap$StrongKeyDummyValueSegment<K> self() {
      return this;
   }

   public MapMakerInternalMap$StrongKeyDummyValueEntry<K> castForTesting(MapMakerInternalMap$InternalEntry<K, MapMaker$Dummy, ?> entry) {
      return (MapMakerInternalMap$StrongKeyDummyValueEntry)entry;
   }
}

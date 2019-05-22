package com.google.common.collect;

import java.lang.ref.ReferenceQueue;

final class MapMakerInternalMap$WeakKeyDummyValueSegment<K> extends MapMakerInternalMap.Segment<K, MapMaker$Dummy, MapMakerInternalMap$WeakKeyDummyValueEntry<K>, MapMakerInternalMap$WeakKeyDummyValueSegment<K>> {
   private final ReferenceQueue<K> queueForKeys = new ReferenceQueue();

   MapMakerInternalMap$WeakKeyDummyValueSegment(MapMakerInternalMap<K, MapMaker$Dummy, MapMakerInternalMap$WeakKeyDummyValueEntry<K>, MapMakerInternalMap$WeakKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
      super(map, initialCapacity, maxSegmentSize);
   }

   MapMakerInternalMap$WeakKeyDummyValueSegment<K> self() {
      return this;
   }

   ReferenceQueue<K> getKeyReferenceQueueForTesting() {
      return this.queueForKeys;
   }

   public MapMakerInternalMap$WeakKeyDummyValueEntry<K> castForTesting(MapMakerInternalMap$InternalEntry<K, MapMaker$Dummy, ?> entry) {
      return (MapMakerInternalMap$WeakKeyDummyValueEntry)entry;
   }

   void maybeDrainReferenceQueues() {
      this.drainKeyReferenceQueue(this.queueForKeys);
   }

   void maybeClearReferenceQueues() {
      this.clearReferenceQueue(this.queueForKeys);
   }

   // $FF: synthetic method
   static ReferenceQueue access$200(MapMakerInternalMap$WeakKeyDummyValueSegment x0) {
      return x0.queueForKeys;
   }
}

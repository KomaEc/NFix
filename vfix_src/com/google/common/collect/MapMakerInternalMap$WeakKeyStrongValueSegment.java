package com.google.common.collect;

import java.lang.ref.ReferenceQueue;

final class MapMakerInternalMap$WeakKeyStrongValueSegment<K, V> extends MapMakerInternalMap.Segment<K, V, MapMakerInternalMap$WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap$WeakKeyStrongValueSegment<K, V>> {
   private final ReferenceQueue<K> queueForKeys = new ReferenceQueue();

   MapMakerInternalMap$WeakKeyStrongValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap$WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap$WeakKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
      super(map, initialCapacity, maxSegmentSize);
   }

   MapMakerInternalMap$WeakKeyStrongValueSegment<K, V> self() {
      return this;
   }

   ReferenceQueue<K> getKeyReferenceQueueForTesting() {
      return this.queueForKeys;
   }

   public MapMakerInternalMap$WeakKeyStrongValueEntry<K, V> castForTesting(MapMakerInternalMap$InternalEntry<K, V, ?> entry) {
      return (MapMakerInternalMap$WeakKeyStrongValueEntry)entry;
   }

   void maybeDrainReferenceQueues() {
      this.drainKeyReferenceQueue(this.queueForKeys);
   }

   void maybeClearReferenceQueues() {
      this.clearReferenceQueue(this.queueForKeys);
   }

   // $FF: synthetic method
   static ReferenceQueue access$300(MapMakerInternalMap$WeakKeyStrongValueSegment x0) {
      return x0.queueForKeys;
   }
}

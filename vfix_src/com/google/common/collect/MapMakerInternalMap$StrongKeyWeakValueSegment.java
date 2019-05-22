package com.google.common.collect;

import java.lang.ref.ReferenceQueue;

final class MapMakerInternalMap$StrongKeyWeakValueSegment<K, V> extends MapMakerInternalMap.Segment<K, V, MapMakerInternalMap$StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap$StrongKeyWeakValueSegment<K, V>> {
   private final ReferenceQueue<V> queueForValues = new ReferenceQueue();

   MapMakerInternalMap$StrongKeyWeakValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap$StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap$StrongKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
      super(map, initialCapacity, maxSegmentSize);
   }

   MapMakerInternalMap$StrongKeyWeakValueSegment<K, V> self() {
      return this;
   }

   ReferenceQueue<V> getValueReferenceQueueForTesting() {
      return this.queueForValues;
   }

   public MapMakerInternalMap$StrongKeyWeakValueEntry<K, V> castForTesting(MapMakerInternalMap$InternalEntry<K, V, ?> entry) {
      return (MapMakerInternalMap$StrongKeyWeakValueEntry)entry;
   }

   public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap$StrongKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(MapMakerInternalMap$InternalEntry<K, V, ?> e) {
      return this.castForTesting(e).getValueReference();
   }

   public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap$StrongKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(MapMakerInternalMap$InternalEntry<K, V, ?> e, V value) {
      return new MapMakerInternalMap$WeakValueReferenceImpl(this.queueForValues, value, this.castForTesting(e));
   }

   public void setWeakValueReferenceForTesting(MapMakerInternalMap$InternalEntry<K, V, ?> e, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap$InternalEntry<K, V, ?>> valueReference) {
      MapMakerInternalMap$StrongKeyWeakValueEntry<K, V> entry = this.castForTesting(e);
      MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap$StrongKeyWeakValueEntry<K, V>> previous = MapMakerInternalMap$StrongKeyWeakValueEntry.access$600(entry);
      MapMakerInternalMap$StrongKeyWeakValueEntry.access$602(entry, valueReference);
      previous.clear();
   }

   void maybeDrainReferenceQueues() {
      this.drainValueReferenceQueue(this.queueForValues);
   }

   void maybeClearReferenceQueues() {
      this.clearReferenceQueue(this.queueForValues);
   }

   // $FF: synthetic method
   static ReferenceQueue access$100(MapMakerInternalMap$StrongKeyWeakValueSegment x0) {
      return x0.queueForValues;
   }
}

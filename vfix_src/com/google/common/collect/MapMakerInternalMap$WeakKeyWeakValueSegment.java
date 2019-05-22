package com.google.common.collect;

import java.lang.ref.ReferenceQueue;

final class MapMakerInternalMap$WeakKeyWeakValueSegment<K, V> extends MapMakerInternalMap.Segment<K, V, MapMakerInternalMap$WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap$WeakKeyWeakValueSegment<K, V>> {
   private final ReferenceQueue<K> queueForKeys = new ReferenceQueue();
   private final ReferenceQueue<V> queueForValues = new ReferenceQueue();

   MapMakerInternalMap$WeakKeyWeakValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap$WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap$WeakKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
      super(map, initialCapacity, maxSegmentSize);
   }

   MapMakerInternalMap$WeakKeyWeakValueSegment<K, V> self() {
      return this;
   }

   ReferenceQueue<K> getKeyReferenceQueueForTesting() {
      return this.queueForKeys;
   }

   ReferenceQueue<V> getValueReferenceQueueForTesting() {
      return this.queueForValues;
   }

   public MapMakerInternalMap$WeakKeyWeakValueEntry<K, V> castForTesting(MapMakerInternalMap$InternalEntry<K, V, ?> entry) {
      return (MapMakerInternalMap$WeakKeyWeakValueEntry)entry;
   }

   public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap$WeakKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(MapMakerInternalMap$InternalEntry<K, V, ?> e) {
      return this.castForTesting(e).getValueReference();
   }

   public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap$WeakKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(MapMakerInternalMap$InternalEntry<K, V, ?> e, V value) {
      return new MapMakerInternalMap$WeakValueReferenceImpl(this.queueForValues, value, this.castForTesting(e));
   }

   public void setWeakValueReferenceForTesting(MapMakerInternalMap$InternalEntry<K, V, ?> e, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap$InternalEntry<K, V, ?>> valueReference) {
      MapMakerInternalMap$WeakKeyWeakValueEntry<K, V> entry = this.castForTesting(e);
      MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap$WeakKeyWeakValueEntry<K, V>> previous = MapMakerInternalMap$WeakKeyWeakValueEntry.access$700(entry);
      MapMakerInternalMap$WeakKeyWeakValueEntry.access$702(entry, valueReference);
      previous.clear();
   }

   void maybeDrainReferenceQueues() {
      this.drainKeyReferenceQueue(this.queueForKeys);
      this.drainValueReferenceQueue(this.queueForValues);
   }

   void maybeClearReferenceQueues() {
      this.clearReferenceQueue(this.queueForKeys);
   }

   // $FF: synthetic method
   static ReferenceQueue access$400(MapMakerInternalMap$WeakKeyWeakValueSegment x0) {
      return x0.queueForKeys;
   }

   // $FF: synthetic method
   static ReferenceQueue access$500(MapMakerInternalMap$WeakKeyWeakValueSegment x0) {
      return x0.queueForValues;
   }
}

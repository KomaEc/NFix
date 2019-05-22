package com.google.common.collect;

import java.lang.ref.ReferenceQueue;
import javax.annotation.Nullable;

final class MapMakerInternalMap$WeakKeyWeakValueEntry<K, V> extends MapMakerInternalMap$AbstractWeakKeyEntry<K, V, MapMakerInternalMap$WeakKeyWeakValueEntry<K, V>> implements MapMakerInternalMap$WeakValueEntry<K, V, MapMakerInternalMap$WeakKeyWeakValueEntry<K, V>> {
   private volatile MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap$WeakKeyWeakValueEntry<K, V>> valueReference = MapMakerInternalMap.unsetWeakValueReference();

   MapMakerInternalMap$WeakKeyWeakValueEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable MapMakerInternalMap$WeakKeyWeakValueEntry<K, V> next) {
      super(queue, key, hash, next);
   }

   public V getValue() {
      return this.valueReference.get();
   }

   MapMakerInternalMap$WeakKeyWeakValueEntry<K, V> copy(ReferenceQueue<K> queueForKeys, ReferenceQueue<V> queueForValues, MapMakerInternalMap$WeakKeyWeakValueEntry<K, V> newNext) {
      MapMakerInternalMap$WeakKeyWeakValueEntry<K, V> newEntry = new MapMakerInternalMap$WeakKeyWeakValueEntry(queueForKeys, this.getKey(), this.hash, newNext);
      newEntry.valueReference = this.valueReference.copyFor(queueForValues, newEntry);
      return newEntry;
   }

   public void clearValue() {
      this.valueReference.clear();
   }

   void setValue(V value, ReferenceQueue<V> queueForValues) {
      MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap$WeakKeyWeakValueEntry<K, V>> previous = this.valueReference;
      this.valueReference = new MapMakerInternalMap$WeakValueReferenceImpl(queueForValues, value, this);
      previous.clear();
   }

   public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap$WeakKeyWeakValueEntry<K, V>> getValueReference() {
      return this.valueReference;
   }

   // $FF: synthetic method
   static MapMakerInternalMap.WeakValueReference access$700(MapMakerInternalMap$WeakKeyWeakValueEntry x0) {
      return x0.valueReference;
   }

   // $FF: synthetic method
   static MapMakerInternalMap.WeakValueReference access$702(MapMakerInternalMap$WeakKeyWeakValueEntry x0, MapMakerInternalMap.WeakValueReference x1) {
      return x0.valueReference = x1;
   }
}

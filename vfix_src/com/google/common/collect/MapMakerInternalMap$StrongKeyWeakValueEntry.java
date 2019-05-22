package com.google.common.collect;

import java.lang.ref.ReferenceQueue;
import javax.annotation.Nullable;

final class MapMakerInternalMap$StrongKeyWeakValueEntry<K, V> extends MapMakerInternalMap$AbstractStrongKeyEntry<K, V, MapMakerInternalMap$StrongKeyWeakValueEntry<K, V>> implements MapMakerInternalMap$WeakValueEntry<K, V, MapMakerInternalMap$StrongKeyWeakValueEntry<K, V>> {
   private volatile MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap$StrongKeyWeakValueEntry<K, V>> valueReference = MapMakerInternalMap.unsetWeakValueReference();

   MapMakerInternalMap$StrongKeyWeakValueEntry(K key, int hash, @Nullable MapMakerInternalMap$StrongKeyWeakValueEntry<K, V> next) {
      super(key, hash, next);
   }

   public V getValue() {
      return this.valueReference.get();
   }

   public void clearValue() {
      this.valueReference.clear();
   }

   void setValue(V value, ReferenceQueue<V> queueForValues) {
      MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap$StrongKeyWeakValueEntry<K, V>> previous = this.valueReference;
      this.valueReference = new MapMakerInternalMap$WeakValueReferenceImpl(queueForValues, value, this);
      previous.clear();
   }

   MapMakerInternalMap$StrongKeyWeakValueEntry<K, V> copy(ReferenceQueue<V> queueForValues, MapMakerInternalMap$StrongKeyWeakValueEntry<K, V> newNext) {
      MapMakerInternalMap$StrongKeyWeakValueEntry<K, V> newEntry = new MapMakerInternalMap$StrongKeyWeakValueEntry(this.key, this.hash, newNext);
      newEntry.valueReference = this.valueReference.copyFor(queueForValues, newEntry);
      return newEntry;
   }

   public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap$StrongKeyWeakValueEntry<K, V>> getValueReference() {
      return this.valueReference;
   }

   // $FF: synthetic method
   static MapMakerInternalMap.WeakValueReference access$600(MapMakerInternalMap$StrongKeyWeakValueEntry x0) {
      return x0.valueReference;
   }

   // $FF: synthetic method
   static MapMakerInternalMap.WeakValueReference access$602(MapMakerInternalMap$StrongKeyWeakValueEntry x0, MapMakerInternalMap.WeakValueReference x1) {
      return x0.valueReference = x1;
   }
}

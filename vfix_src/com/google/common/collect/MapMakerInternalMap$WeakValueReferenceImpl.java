package com.google.common.collect;

import com.google.j2objc.annotations.Weak;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

final class MapMakerInternalMap$WeakValueReferenceImpl<K, V, E extends MapMakerInternalMap$InternalEntry<K, V, E>> extends WeakReference<V> implements MapMakerInternalMap.WeakValueReference<K, V, E> {
   @Weak
   final E entry;

   MapMakerInternalMap$WeakValueReferenceImpl(ReferenceQueue<V> queue, V referent, E entry) {
      super(referent, queue);
      this.entry = entry;
   }

   public E getEntry() {
      return this.entry;
   }

   public MapMakerInternalMap.WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> queue, E entry) {
      return new MapMakerInternalMap$WeakValueReferenceImpl(queue, this.get(), entry);
   }
}

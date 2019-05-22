package com.google.common.collect;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Equivalence;

@VisibleForTesting
final class Interners$InternerImpl<E> implements Interner<E> {
   @VisibleForTesting
   final MapMakerInternalMap<E, MapMaker$Dummy, ?, ?> map;

   private Interners$InternerImpl(MapMaker mapMaker) {
      this.map = MapMakerInternalMap.createWithDummyValues(mapMaker.keyEquivalence(Equivalence.equals()));
   }

   public E intern(E sample) {
      MapMaker$Dummy sneaky;
      do {
         MapMakerInternalMap$InternalEntry<E, MapMaker$Dummy, ?> entry = this.map.getEntry(sample);
         if (entry != null) {
            E canonical = entry.getKey();
            if (canonical != null) {
               return canonical;
            }
         }

         sneaky = (MapMaker$Dummy)this.map.putIfAbsent(sample, MapMaker$Dummy.VALUE);
      } while(sneaky != null);

      return sample;
   }

   // $FF: synthetic method
   Interners$InternerImpl(MapMaker x0, Object x1) {
      this(x0);
   }
}

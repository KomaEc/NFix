package com.google.common.util.concurrent;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.MapMaker;
import java.util.concurrent.ConcurrentMap;

@VisibleForTesting
class Striped$LargeLazyStriped<L> extends Striped.PowerOfTwoStriped<L> {
   final ConcurrentMap<Integer, L> locks;
   final Supplier<L> supplier;
   final int size;

   Striped$LargeLazyStriped(int stripes, Supplier<L> supplier) {
      super(stripes);
      this.size = this.mask == -1 ? Integer.MAX_VALUE : this.mask + 1;
      this.supplier = supplier;
      this.locks = (new MapMaker()).weakValues().makeMap();
   }

   public L getAt(int index) {
      if (this.size != Integer.MAX_VALUE) {
         Preconditions.checkElementIndex(index, this.size());
      }

      L existing = this.locks.get(index);
      if (existing != null) {
         return existing;
      } else {
         L created = this.supplier.get();
         existing = this.locks.putIfAbsent(index, created);
         return MoreObjects.firstNonNull(existing, created);
      }
   }

   public int size() {
      return this.size;
   }
}

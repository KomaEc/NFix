package com.google.common.util.concurrent;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.atomic.AtomicReferenceArray;

@VisibleForTesting
class Striped$SmallLazyStriped<L> extends Striped.PowerOfTwoStriped<L> {
   final AtomicReferenceArray<Striped$SmallLazyStriped$ArrayReference<? extends L>> locks;
   final Supplier<L> supplier;
   final int size;
   final ReferenceQueue<L> queue = new ReferenceQueue();

   Striped$SmallLazyStriped(int stripes, Supplier<L> supplier) {
      super(stripes);
      this.size = this.mask == -1 ? Integer.MAX_VALUE : this.mask + 1;
      this.locks = new AtomicReferenceArray(this.size);
      this.supplier = supplier;
   }

   public L getAt(int index) {
      if (this.size != Integer.MAX_VALUE) {
         Preconditions.checkElementIndex(index, this.size());
      }

      Striped$SmallLazyStriped$ArrayReference<? extends L> existingRef = (Striped$SmallLazyStriped$ArrayReference)this.locks.get(index);
      L existing = existingRef == null ? null : existingRef.get();
      if (existing != null) {
         return existing;
      } else {
         L created = this.supplier.get();
         Striped$SmallLazyStriped$ArrayReference newRef = new Striped$SmallLazyStriped$ArrayReference(created, index, this.queue);

         do {
            if (this.locks.compareAndSet(index, existingRef, newRef)) {
               this.drainQueue();
               return created;
            }

            existingRef = (Striped$SmallLazyStriped$ArrayReference)this.locks.get(index);
            existing = existingRef == null ? null : existingRef.get();
         } while(existing == null);

         return existing;
      }
   }

   private void drainQueue() {
      Reference ref;
      while((ref = this.queue.poll()) != null) {
         Striped$SmallLazyStriped$ArrayReference<? extends L> arrayRef = (Striped$SmallLazyStriped$ArrayReference)ref;
         this.locks.compareAndSet(arrayRef.index, arrayRef, (Object)null);
      }

   }

   public int size() {
      return this.size;
   }
}

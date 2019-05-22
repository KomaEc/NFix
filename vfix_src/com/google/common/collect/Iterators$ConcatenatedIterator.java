package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;

class Iterators$ConcatenatedIterator<T> implements Iterator<T> {
   private Iterator<? extends T> toRemove;
   private Iterator<? extends T> iterator = Iterators.emptyIterator();
   private Iterator<? extends Iterator<? extends T>> topMetaIterator;
   @Nullable
   private Deque<Iterator<? extends Iterator<? extends T>>> metaIterators;

   Iterators$ConcatenatedIterator(Iterator<? extends Iterator<? extends T>> metaIterator) {
      this.topMetaIterator = (Iterator)Preconditions.checkNotNull(metaIterator);
   }

   @Nullable
   private Iterator<? extends Iterator<? extends T>> getTopMetaIterator() {
      while(this.topMetaIterator == null || !this.topMetaIterator.hasNext()) {
         if (this.metaIterators == null || this.metaIterators.isEmpty()) {
            return null;
         }

         this.topMetaIterator = (Iterator)this.metaIterators.removeFirst();
      }

      return this.topMetaIterator;
   }

   public boolean hasNext() {
      while(!((Iterator)Preconditions.checkNotNull(this.iterator)).hasNext()) {
         this.topMetaIterator = this.getTopMetaIterator();
         if (this.topMetaIterator == null) {
            return false;
         }

         this.iterator = (Iterator)this.topMetaIterator.next();
         if (this.iterator instanceof Iterators$ConcatenatedIterator) {
            Iterators$ConcatenatedIterator<T> topConcat = (Iterators$ConcatenatedIterator)this.iterator;
            this.iterator = topConcat.iterator;
            if (this.metaIterators == null) {
               this.metaIterators = new ArrayDeque();
            }

            this.metaIterators.addFirst(this.topMetaIterator);
            if (topConcat.metaIterators != null) {
               while(!topConcat.metaIterators.isEmpty()) {
                  this.metaIterators.addFirst(topConcat.metaIterators.removeLast());
               }
            }

            this.topMetaIterator = topConcat.topMetaIterator;
         }
      }

      return true;
   }

   public T next() {
      if (this.hasNext()) {
         this.toRemove = this.iterator;
         return this.iterator.next();
      } else {
         throw new NoSuchElementException();
      }
   }

   public void remove() {
      CollectPreconditions.checkRemove(this.toRemove != null);
      this.toRemove.remove();
      this.toRemove = null;
   }
}

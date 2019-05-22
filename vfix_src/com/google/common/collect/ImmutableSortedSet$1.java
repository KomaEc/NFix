package com.google.common.collect;

import java.util.Comparator;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;

class ImmutableSortedSet$1 extends AbstractSpliterator<E> {
   final UnmodifiableIterator<E> iterator;
   // $FF: synthetic field
   final ImmutableSortedSet this$0;

   ImmutableSortedSet$1(ImmutableSortedSet this$0, long x0, int x1) {
      super(x0, x1);
      this.this$0 = this$0;
      this.iterator = this.this$0.iterator();
   }

   public boolean tryAdvance(Consumer<? super E> action) {
      if (this.iterator.hasNext()) {
         action.accept(this.iterator.next());
         return true;
      } else {
         return false;
      }
   }

   public Comparator<? super E> getComparator() {
      return this.this$0.comparator;
   }
}

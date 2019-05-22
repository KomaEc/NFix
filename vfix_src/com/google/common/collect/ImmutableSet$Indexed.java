package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Spliterator;
import java.util.function.Consumer;

abstract class ImmutableSet$Indexed<E> extends ImmutableSet<E> {
   abstract E get(int var1);

   public UnmodifiableIterator<E> iterator() {
      return this.asList().iterator();
   }

   public Spliterator<E> spliterator() {
      return CollectSpliterators.indexed(this.size(), 1297, this::get);
   }

   public void forEach(Consumer<? super E> consumer) {
      Preconditions.checkNotNull(consumer);
      int n = this.size();

      for(int i = 0; i < n; ++i) {
         consumer.accept(this.get(i));
      }

   }

   ImmutableList<E> createAsList() {
      return new ImmutableSet$Indexed$1(this);
   }
}

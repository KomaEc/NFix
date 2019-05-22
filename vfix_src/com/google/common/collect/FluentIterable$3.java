package com.google.common.collect;

import java.util.Iterator;

final class FluentIterable$3 extends FluentIterable<T> {
   // $FF: synthetic field
   final Iterable val$inputs;

   FluentIterable$3(Iterable var1) {
      this.val$inputs = var1;
   }

   public Iterator<T> iterator() {
      return Iterators.concat(Iterators.transform(this.val$inputs.iterator(), Iterables.toIterator()));
   }
}

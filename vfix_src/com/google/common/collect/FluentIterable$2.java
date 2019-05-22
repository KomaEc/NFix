package com.google.common.collect;

import java.util.Iterator;

final class FluentIterable$2 extends FluentIterable<T> {
   // $FF: synthetic field
   final Iterable[] val$inputs;

   FluentIterable$2(Iterable[] var1) {
      this.val$inputs = var1;
   }

   public Iterator<T> iterator() {
      return Iterators.concat((Iterator)(new FluentIterable$2$1(this, this.val$inputs.length)));
   }
}

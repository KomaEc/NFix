package com.google.common.collect;

import java.util.Iterator;

class FluentIterable$2$1 extends AbstractIndexedListIterator<Iterator<? extends T>> {
   // $FF: synthetic field
   final FluentIterable$2 this$0;

   FluentIterable$2$1(FluentIterable$2 this$0, int size) {
      super(size);
      this.this$0 = this$0;
   }

   public Iterator<? extends T> get(int i) {
      return this.this$0.val$inputs[i].iterator();
   }
}

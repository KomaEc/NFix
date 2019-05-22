package com.google.common.collect;

import java.util.Iterator;
import java.util.Map.Entry;

class StandardTable$Row$1 implements Iterator<Entry<C, V>> {
   // $FF: synthetic field
   final Iterator val$iterator;
   // $FF: synthetic field
   final StandardTable.Row this$1;

   StandardTable$Row$1(StandardTable.Row this$1, Iterator var2) {
      this.this$1 = this$1;
      this.val$iterator = var2;
   }

   public boolean hasNext() {
      return this.val$iterator.hasNext();
   }

   public Entry<C, V> next() {
      return this.this$1.wrapEntry((Entry)this.val$iterator.next());
   }

   public void remove() {
      this.val$iterator.remove();
      this.this$1.maintainEmptyInvariant();
   }
}

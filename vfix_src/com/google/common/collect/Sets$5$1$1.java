package com.google.common.collect;

import java.util.AbstractSet;
import java.util.BitSet;
import java.util.Iterator;
import javax.annotation.Nullable;

class Sets$5$1$1 extends AbstractSet<E> {
   // $FF: synthetic field
   final BitSet val$copy;
   // $FF: synthetic field
   final Sets$5$1 this$1;

   Sets$5$1$1(Sets$5$1 this$1, BitSet var2) {
      this.this$1 = this$1;
      this.val$copy = var2;
   }

   public boolean contains(@Nullable Object o) {
      Integer i = (Integer)this.this$1.this$0.val$index.get(o);
      return i != null && this.val$copy.get(i);
   }

   public Iterator<E> iterator() {
      return new Sets$5$1$1$1(this);
   }

   public int size() {
      return this.this$1.this$0.val$size;
   }
}

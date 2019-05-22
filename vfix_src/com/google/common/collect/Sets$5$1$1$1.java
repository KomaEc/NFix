package com.google.common.collect;

class Sets$5$1$1$1 extends AbstractIterator<E> {
   int i;
   // $FF: synthetic field
   final Sets$5$1$1 this$2;

   Sets$5$1$1$1(Sets$5$1$1 this$2) {
      this.this$2 = this$2;
      this.i = -1;
   }

   protected E computeNext() {
      this.i = this.this$2.val$copy.nextSetBit(this.i + 1);
      return this.i == -1 ? this.endOfData() : this.this$2.this$1.this$0.val$index.keySet().asList().get(this.i);
   }
}

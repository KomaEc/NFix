package com.google.common.collect;

class EnumMultiset$2$1 extends Multisets.AbstractEntry<E> {
   // $FF: synthetic field
   final int val$index;
   // $FF: synthetic field
   final EnumMultiset$2 this$1;

   EnumMultiset$2$1(EnumMultiset$2 this$1, int var2) {
      this.this$1 = this$1;
      this.val$index = var2;
   }

   public E getElement() {
      return EnumMultiset.access$000(this.this$1.this$0)[this.val$index];
   }

   public int getCount() {
      return EnumMultiset.access$100(this.this$1.this$0)[this.val$index];
   }
}

package com.google.common.collect;

class EnumMultiset$1$1 extends EnumMultiset<E>.Itr<E> {
   // $FF: synthetic field
   final EnumMultiset$1 this$1;

   EnumMultiset$1$1(EnumMultiset$1 this$1) {
      super(this$1.this$0);
      this.this$1 = this$1;
   }

   E output(int index) {
      return EnumMultiset.access$000(this.this$1.this$0)[index];
   }
}

package com.google.common.collect;

class EnumMultiset$2 extends EnumMultiset<E>.Itr<Multiset.Entry<E>> {
   // $FF: synthetic field
   final EnumMultiset this$0;

   EnumMultiset$2(EnumMultiset this$0) {
      super(this$0);
      this.this$0 = this$0;
   }

   Multiset.Entry<E> output(int index) {
      return new EnumMultiset$2$1(this, index);
   }
}

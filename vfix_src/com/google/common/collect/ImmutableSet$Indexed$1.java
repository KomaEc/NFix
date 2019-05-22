package com.google.common.collect;

class ImmutableSet$Indexed$1 extends ImmutableAsList<E> {
   // $FF: synthetic field
   final ImmutableSet$Indexed this$0;

   ImmutableSet$Indexed$1(ImmutableSet$Indexed this$0) {
      this.this$0 = this$0;
   }

   public E get(int index) {
      return this.this$0.get(index);
   }

   ImmutableSet$Indexed<E> delegateCollection() {
      return this.this$0;
   }
}

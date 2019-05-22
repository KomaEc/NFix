package com.google.common.collect;

import com.google.common.base.Preconditions;

class RegularContiguousSet$3 extends ImmutableAsList<C> {
   // $FF: synthetic field
   final RegularContiguousSet this$0;

   RegularContiguousSet$3(RegularContiguousSet this$0) {
      this.this$0 = this$0;
   }

   ImmutableSortedSet<C> delegateCollection() {
      return this.this$0;
   }

   public C get(int i) {
      Preconditions.checkElementIndex(i, this.size());
      return this.this$0.domain.offset(this.this$0.first(), (long)i);
   }
}

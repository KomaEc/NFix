package com.google.common.collect;

import javax.annotation.Nullable;

final class RegularImmutableMultiset$ElementSet extends ImmutableSet$Indexed<E> {
   // $FF: synthetic field
   final RegularImmutableMultiset this$0;

   private RegularImmutableMultiset$ElementSet(RegularImmutableMultiset var1) {
      this.this$0 = var1;
   }

   E get(int index) {
      return RegularImmutableMultiset.access$100(this.this$0)[index].getElement();
   }

   public boolean contains(@Nullable Object object) {
      return this.this$0.contains(object);
   }

   boolean isPartialView() {
      return true;
   }

   public int size() {
      return RegularImmutableMultiset.access$100(this.this$0).length;
   }

   // $FF: synthetic method
   RegularImmutableMultiset$ElementSet(RegularImmutableMultiset x0, RegularImmutableMultiset$1 x1) {
      this(x0);
   }
}

package com.google.common.collect;

import java.util.Iterator;

class EnumMultiset$1 extends AbstractMultiset<E>.ElementSet {
   // $FF: synthetic field
   final EnumMultiset this$0;

   EnumMultiset$1(EnumMultiset this$0) {
      super();
      this.this$0 = this$0;
   }

   public Iterator<E> iterator() {
      return new EnumMultiset$1$1(this);
   }
}

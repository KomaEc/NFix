package com.google.common.collect;

import java.util.BitSet;
import java.util.Set;

class Sets$5$1 extends AbstractIterator<Set<E>> {
   final BitSet bits;
   // $FF: synthetic field
   final Sets$5 this$0;

   Sets$5$1(Sets$5 this$0) {
      this.this$0 = this$0;
      this.bits = new BitSet(this.this$0.val$index.size());
   }

   protected Set<E> computeNext() {
      if (this.bits.isEmpty()) {
         this.bits.set(0, this.this$0.val$size);
      } else {
         int firstSetBit = this.bits.nextSetBit(0);
         int bitToFlip = this.bits.nextClearBit(firstSetBit);
         if (bitToFlip == this.this$0.val$index.size()) {
            return (Set)this.endOfData();
         }

         this.bits.set(0, bitToFlip - firstSetBit - 1);
         this.bits.clear(bitToFlip - firstSetBit - 1, bitToFlip);
         this.bits.set(bitToFlip);
      }

      BitSet copy = (BitSet)this.bits.clone();
      return new Sets$5$1$1(this, copy);
   }
}

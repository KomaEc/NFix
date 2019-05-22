package com.google.common.collect;

import java.util.Iterator;

class Sets$4$1 extends AbstractIterator<E> {
   // $FF: synthetic field
   final Iterator val$itr1;
   // $FF: synthetic field
   final Iterator val$itr2;
   // $FF: synthetic field
   final Sets$4 this$0;

   Sets$4$1(Sets$4 this$0, Iterator var2, Iterator var3) {
      this.this$0 = this$0;
      this.val$itr1 = var2;
      this.val$itr2 = var3;
   }

   public E computeNext() {
      while(true) {
         Object elem2;
         if (this.val$itr1.hasNext()) {
            elem2 = this.val$itr1.next();
            if (this.this$0.val$set2.contains(elem2)) {
               continue;
            }

            return elem2;
         }

         do {
            if (!this.val$itr2.hasNext()) {
               return this.endOfData();
            }

            elem2 = this.val$itr2.next();
         } while(this.this$0.val$set1.contains(elem2));

         return elem2;
      }
   }
}

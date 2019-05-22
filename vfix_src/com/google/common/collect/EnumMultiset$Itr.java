package com.google.common.collect;

import java.util.Iterator;
import java.util.NoSuchElementException;

abstract class EnumMultiset$Itr<T> implements Iterator<T> {
   int index;
   int toRemove;
   // $FF: synthetic field
   final EnumMultiset this$0;

   EnumMultiset$Itr(EnumMultiset this$0) {
      this.this$0 = this$0;
      this.index = 0;
      this.toRemove = -1;
   }

   abstract T output(int var1);

   public boolean hasNext() {
      while(this.index < EnumMultiset.access$000(this.this$0).length) {
         if (EnumMultiset.access$100(this.this$0)[this.index] > 0) {
            return true;
         }

         ++this.index;
      }

      return false;
   }

   public T next() {
      if (!this.hasNext()) {
         throw new NoSuchElementException();
      } else {
         T result = this.output(this.index);
         this.toRemove = this.index++;
         return result;
      }
   }

   public void remove() {
      CollectPreconditions.checkRemove(this.toRemove >= 0);
      if (EnumMultiset.access$100(this.this$0)[this.toRemove] > 0) {
         EnumMultiset.access$210(this.this$0);
         EnumMultiset.access$302(this.this$0, EnumMultiset.access$300(this.this$0) - (long)EnumMultiset.access$100(this.this$0)[this.toRemove]);
         EnumMultiset.access$100(this.this$0)[this.toRemove] = 0;
      }

      this.toRemove = -1;
   }
}

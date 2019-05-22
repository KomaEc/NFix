package com.google.common.collect;

import java.util.Iterator;
import java.util.Set;

final class Sets$4 extends Sets.SetView<E> {
   // $FF: synthetic field
   final Set val$set1;
   // $FF: synthetic field
   final Set val$set2;

   Sets$4(Set var1, Set var2) {
      super(null);
      this.val$set1 = var1;
      this.val$set2 = var2;
   }

   public UnmodifiableIterator<E> iterator() {
      Iterator<? extends E> itr1 = this.val$set1.iterator();
      Iterator<? extends E> itr2 = this.val$set2.iterator();
      return new Sets$4$1(this, itr1, itr2);
   }

   public int size() {
      int size = 0;
      Iterator var2 = this.val$set1.iterator();

      Object e;
      while(var2.hasNext()) {
         e = var2.next();
         if (!this.val$set2.contains(e)) {
            ++size;
         }
      }

      var2 = this.val$set2.iterator();

      while(var2.hasNext()) {
         e = var2.next();
         if (!this.val$set1.contains(e)) {
            ++size;
         }
      }

      return size;
   }

   public boolean isEmpty() {
      return this.val$set1.equals(this.val$set2);
   }

   public boolean contains(Object element) {
      return this.val$set1.contains(element) ^ this.val$set2.contains(element);
   }
}

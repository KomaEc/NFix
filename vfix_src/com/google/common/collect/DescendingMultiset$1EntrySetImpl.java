package com.google.common.collect;

import java.util.Iterator;

class DescendingMultiset$1EntrySetImpl extends Multisets.EntrySet<E> {
   // $FF: synthetic field
   final DescendingMultiset this$0;

   DescendingMultiset$1EntrySetImpl(DescendingMultiset this$0) {
      this.this$0 = this$0;
   }

   Multiset<E> multiset() {
      return this.this$0;
   }

   public Iterator<Multiset.Entry<E>> iterator() {
      return this.this$0.entryIterator();
   }

   public int size() {
      return this.this$0.forwardMultiset().entrySet().size();
   }
}

package com.google.common.collect;

import java.util.AbstractSequentialList;
import java.util.ListIterator;

class LinkedListMultimap$1ValuesImpl extends AbstractSequentialList<V> {
   // $FF: synthetic field
   final LinkedListMultimap this$0;

   LinkedListMultimap$1ValuesImpl(LinkedListMultimap this$0) {
      this.this$0 = this$0;
   }

   public int size() {
      return LinkedListMultimap.access$900(this.this$0);
   }

   public ListIterator<V> listIterator(int index) {
      LinkedListMultimap<K, V>.NodeIterator nodeItr = this.this$0.new NodeIterator(index);
      return new LinkedListMultimap$1ValuesImpl$1(this, nodeItr, nodeItr);
   }
}

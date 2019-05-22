package com.google.common.collect;

import java.util.ListIterator;
import java.util.Map.Entry;

class LinkedListMultimap$1ValuesImpl$1 extends TransformedListIterator<Entry<K, V>, V> {
   // $FF: synthetic field
   final LinkedListMultimap.NodeIterator val$nodeItr;
   // $FF: synthetic field
   final LinkedListMultimap$1ValuesImpl this$1;

   LinkedListMultimap$1ValuesImpl$1(LinkedListMultimap$1ValuesImpl this$1, ListIterator backingIterator, LinkedListMultimap.NodeIterator var3) {
      super(backingIterator);
      this.this$1 = this$1;
      this.val$nodeItr = var3;
   }

   V transform(Entry<K, V> entry) {
      return entry.getValue();
   }

   public void set(V value) {
      this.val$nodeItr.setValue(value);
   }
}

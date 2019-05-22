package com.google.common.collect;

import java.util.Map.Entry;

class ArrayTable$ArrayMap$2 extends AbstractIndexedListIterator<Entry<K, V>> {
   // $FF: synthetic field
   final ArrayTable.ArrayMap this$0;

   ArrayTable$ArrayMap$2(ArrayTable.ArrayMap this$0, int size) {
      super(size);
      this.this$0 = this$0;
   }

   protected Entry<K, V> get(int index) {
      return this.this$0.getEntry(index);
   }
}

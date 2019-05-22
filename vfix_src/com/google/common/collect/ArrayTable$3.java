package com.google.common.collect;

class ArrayTable$3 extends AbstractIndexedListIterator<V> {
   // $FF: synthetic field
   final ArrayTable this$0;

   ArrayTable$3(ArrayTable this$0, int size) {
      super(size);
      this.this$0 = this$0;
   }

   protected V get(int index) {
      return ArrayTable.access$800(this.this$0, index);
   }
}

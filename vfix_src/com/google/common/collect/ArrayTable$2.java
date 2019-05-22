package com.google.common.collect;

class ArrayTable$2 extends Tables.AbstractCell<R, C, V> {
   final int rowIndex;
   final int columnIndex;
   // $FF: synthetic field
   final int val$index;
   // $FF: synthetic field
   final ArrayTable this$0;

   ArrayTable$2(ArrayTable this$0, int var2) {
      this.this$0 = this$0;
      this.val$index = var2;
      this.rowIndex = this.val$index / ArrayTable.access$100(this.this$0).size();
      this.columnIndex = this.val$index % ArrayTable.access$100(this.this$0).size();
   }

   public R getRowKey() {
      return ArrayTable.access$200(this.this$0).get(this.rowIndex);
   }

   public C getColumnKey() {
      return ArrayTable.access$100(this.this$0).get(this.columnIndex);
   }

   public V getValue() {
      return this.this$0.at(this.rowIndex, this.columnIndex);
   }
}

package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Map.Entry;

class StandardTable$Column$EntrySetIterator$1EntryImpl extends AbstractMapEntry<R, V> {
   // $FF: synthetic field
   final Entry val$entry;
   // $FF: synthetic field
   final StandardTable.Column.EntrySetIterator this$2;

   StandardTable$Column$EntrySetIterator$1EntryImpl(StandardTable.Column.EntrySetIterator this$2, Entry var2) {
      this.this$2 = this$2;
      this.val$entry = var2;
   }

   public R getKey() {
      return this.val$entry.getKey();
   }

   public V getValue() {
      return ((Map)this.val$entry.getValue()).get(this.this$2.this$1.columnKey);
   }

   public V setValue(V value) {
      return ((Map)this.val$entry.getValue()).put(this.this$2.this$1.columnKey, Preconditions.checkNotNull(value));
   }
}

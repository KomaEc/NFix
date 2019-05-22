package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Map.Entry;

class StandardTable$Row$2 extends ForwardingMapEntry<C, V> {
   // $FF: synthetic field
   final Entry val$entry;
   // $FF: synthetic field
   final StandardTable.Row this$1;

   StandardTable$Row$2(StandardTable.Row this$1, Entry var2) {
      this.this$1 = this$1;
      this.val$entry = var2;
   }

   protected Entry<C, V> delegate() {
      return this.val$entry;
   }

   public V setValue(V value) {
      return super.setValue(Preconditions.checkNotNull(value));
   }

   public boolean equals(Object object) {
      return this.standardEquals(object);
   }
}

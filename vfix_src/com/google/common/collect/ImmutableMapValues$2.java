package com.google.common.collect;

import java.util.Map.Entry;

class ImmutableMapValues$2 extends ImmutableAsList<V> {
   // $FF: synthetic field
   final ImmutableList val$entryList;
   // $FF: synthetic field
   final ImmutableMapValues this$0;

   ImmutableMapValues$2(ImmutableMapValues this$0, ImmutableList var2) {
      this.this$0 = this$0;
      this.val$entryList = var2;
   }

   public V get(int index) {
      return ((Entry)this.val$entryList.get(index)).getValue();
   }

   ImmutableCollection<V> delegateCollection() {
      return this.this$0;
   }
}

package com.google.common.collect;

import java.util.Map.Entry;

class ImmutableMap$1 extends UnmodifiableIterator<K> {
   // $FF: synthetic field
   final UnmodifiableIterator val$entryIterator;
   // $FF: synthetic field
   final ImmutableMap this$0;

   ImmutableMap$1(ImmutableMap this$0, UnmodifiableIterator var2) {
      this.this$0 = this$0;
      this.val$entryIterator = var2;
   }

   public boolean hasNext() {
      return this.val$entryIterator.hasNext();
   }

   public K next() {
      return ((Entry)this.val$entryIterator.next()).getKey();
   }
}

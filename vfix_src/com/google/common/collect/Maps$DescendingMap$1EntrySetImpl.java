package com.google.common.collect;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

class Maps$DescendingMap$1EntrySetImpl extends Maps.EntrySet<K, V> {
   // $FF: synthetic field
   final Maps.DescendingMap this$0;

   Maps$DescendingMap$1EntrySetImpl(Maps.DescendingMap this$0) {
      this.this$0 = this$0;
   }

   Map<K, V> map() {
      return this.this$0;
   }

   public Iterator<Entry<K, V>> iterator() {
      return this.this$0.entryIterator();
   }
}

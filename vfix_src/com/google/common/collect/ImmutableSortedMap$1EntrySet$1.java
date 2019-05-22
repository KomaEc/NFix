package com.google.common.collect;

import java.util.Spliterator;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;

class ImmutableSortedMap$1EntrySet$1 extends ImmutableAsList<Entry<K, V>> {
   // $FF: synthetic field
   final ImmutableSortedMap$1EntrySet this$1;

   ImmutableSortedMap$1EntrySet$1(ImmutableSortedMap$1EntrySet this$1) {
      this.this$1 = this$1;
   }

   public Entry<K, V> get(int index) {
      return new SimpleImmutableEntry(ImmutableSortedMap.access$200(this.this$1.this$0).asList().get(index), ImmutableSortedMap.access$300(this.this$1.this$0).get(index));
   }

   public Spliterator<Entry<K, V>> spliterator() {
      return CollectSpliterators.indexed(this.size(), 1297, this::get);
   }

   ImmutableCollection<Entry<K, V>> delegateCollection() {
      return this.this$1;
   }
}

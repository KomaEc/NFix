package com.google.common.collect;

import java.util.Spliterator;
import java.util.Map.Entry;
import java.util.function.Consumer;

class ImmutableSortedMap$1EntrySet extends ImmutableMapEntrySet<K, V> {
   // $FF: synthetic field
   final ImmutableSortedMap this$0;

   ImmutableSortedMap$1EntrySet(ImmutableSortedMap this$0) {
      this.this$0 = this$0;
   }

   public UnmodifiableIterator<Entry<K, V>> iterator() {
      return this.asList().iterator();
   }

   public Spliterator<Entry<K, V>> spliterator() {
      return this.asList().spliterator();
   }

   public void forEach(Consumer<? super Entry<K, V>> action) {
      this.asList().forEach(action);
   }

   ImmutableList<Entry<K, V>> createAsList() {
      return new ImmutableSortedMap$1EntrySet$1(this);
   }

   ImmutableMap<K, V> map() {
      return this.this$0;
   }
}

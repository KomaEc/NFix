package com.google.common.collect;

import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.Map.Entry;
import java.util.function.Consumer;

class Maps$IteratorBasedAbstractMap$1 extends Maps.EntrySet<K, V> {
   // $FF: synthetic field
   final Maps$IteratorBasedAbstractMap this$0;

   Maps$IteratorBasedAbstractMap$1(Maps$IteratorBasedAbstractMap this$0) {
      this.this$0 = this$0;
   }

   Map<K, V> map() {
      return this.this$0;
   }

   public Iterator<Entry<K, V>> iterator() {
      return this.this$0.entryIterator();
   }

   public Spliterator<Entry<K, V>> spliterator() {
      return this.this$0.entrySpliterator();
   }

   public void forEach(Consumer<? super Entry<K, V>> action) {
      this.this$0.forEachEntry(action);
   }
}

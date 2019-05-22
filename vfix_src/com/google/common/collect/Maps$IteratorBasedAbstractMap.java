package com.google.common.collect;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Map.Entry;
import java.util.function.Consumer;

abstract class Maps$IteratorBasedAbstractMap<K, V> extends AbstractMap<K, V> {
   public abstract int size();

   abstract Iterator<Entry<K, V>> entryIterator();

   Spliterator<Entry<K, V>> entrySpliterator() {
      return Spliterators.spliterator(this.entryIterator(), (long)this.size(), 65);
   }

   public Set<Entry<K, V>> entrySet() {
      return new Maps$IteratorBasedAbstractMap$1(this);
   }

   void forEachEntry(Consumer<? super Entry<K, V>> action) {
      this.entryIterator().forEachRemaining(action);
   }

   public void clear() {
      Iterators.clear(this.entryIterator());
   }
}

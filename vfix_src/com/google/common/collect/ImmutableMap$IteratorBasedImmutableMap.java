package com.google.common.collect;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Map.Entry;

abstract class ImmutableMap$IteratorBasedImmutableMap<K, V> extends ImmutableMap<K, V> {
   abstract UnmodifiableIterator<Entry<K, V>> entryIterator();

   Spliterator<Entry<K, V>> entrySpliterator() {
      return Spliterators.spliterator(this.entryIterator(), (long)this.size(), 1297);
   }

   ImmutableSet<K> createKeySet() {
      return new ImmutableMapKeySet(this);
   }

   ImmutableSet<Entry<K, V>> createEntrySet() {
      return new ImmutableMap$IteratorBasedImmutableMap$1EntrySetImpl(this);
   }

   ImmutableCollection<V> createValues() {
      return new ImmutableMapValues(this);
   }
}

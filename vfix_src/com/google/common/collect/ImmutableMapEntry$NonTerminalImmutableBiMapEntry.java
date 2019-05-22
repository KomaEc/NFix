package com.google.common.collect;

import javax.annotation.Nullable;

final class ImmutableMapEntry$NonTerminalImmutableBiMapEntry<K, V> extends ImmutableMapEntry$NonTerminalImmutableMapEntry<K, V> {
   private final transient ImmutableMapEntry<K, V> nextInValueBucket;

   ImmutableMapEntry$NonTerminalImmutableBiMapEntry(K key, V value, ImmutableMapEntry<K, V> nextInKeyBucket, ImmutableMapEntry<K, V> nextInValueBucket) {
      super(key, value, nextInKeyBucket);
      this.nextInValueBucket = nextInValueBucket;
   }

   @Nullable
   ImmutableMapEntry<K, V> getNextInValueBucket() {
      return this.nextInValueBucket;
   }
}

package com.google.common.collect;

import javax.annotation.Nullable;

class ImmutableMapEntry$NonTerminalImmutableMapEntry<K, V> extends ImmutableMapEntry<K, V> {
   private final transient ImmutableMapEntry<K, V> nextInKeyBucket;

   ImmutableMapEntry$NonTerminalImmutableMapEntry(K key, V value, ImmutableMapEntry<K, V> nextInKeyBucket) {
      super(key, value);
      this.nextInKeyBucket = nextInKeyBucket;
   }

   @Nullable
   final ImmutableMapEntry<K, V> getNextInKeyBucket() {
      return this.nextInKeyBucket;
   }

   final boolean isReusable() {
      return false;
   }
}

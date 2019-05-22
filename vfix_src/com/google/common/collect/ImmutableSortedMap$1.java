package com.google.common.collect;

import java.util.Comparator;
import java.util.Map.Entry;

final class ImmutableSortedMap$1 implements Comparator<Entry<K, V>> {
   // $FF: synthetic field
   final Comparator val$comparator;

   ImmutableSortedMap$1(Comparator var1) {
      this.val$comparator = var1;
   }

   public int compare(Entry<K, V> e1, Entry<K, V> e2) {
      return this.val$comparator.compare(e1.getKey(), e2.getKey());
   }
}

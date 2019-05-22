package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.j2objc.annotations.Weak;

@GwtCompatible(
   emulated = true
)
final class RegularImmutableMap$Values<K, V> extends ImmutableList<V> {
   @Weak
   final RegularImmutableMap<K, V> map;

   RegularImmutableMap$Values(RegularImmutableMap<K, V> map) {
      this.map = map;
   }

   public V get(int index) {
      return this.map.entries[index].getValue();
   }

   public int size() {
      return this.map.size();
   }

   boolean isPartialView() {
      return true;
   }

   @GwtIncompatible
   Object writeReplace() {
      return new RegularImmutableMap$Values$SerializedForm(this.map);
   }
}

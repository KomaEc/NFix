package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.j2objc.annotations.Weak;

@GwtCompatible(
   emulated = true
)
final class RegularImmutableMap$KeySet<K, V> extends ImmutableSet$Indexed<K> {
   @Weak
   private final RegularImmutableMap<K, V> map;

   RegularImmutableMap$KeySet(RegularImmutableMap<K, V> map) {
      this.map = map;
   }

   K get(int index) {
      return this.map.entries[index].getKey();
   }

   public boolean contains(Object object) {
      return this.map.containsKey(object);
   }

   boolean isPartialView() {
      return true;
   }

   public int size() {
      return this.map.size();
   }

   @GwtIncompatible
   Object writeReplace() {
      return new RegularImmutableMap$KeySet$SerializedForm(this.map);
   }
}

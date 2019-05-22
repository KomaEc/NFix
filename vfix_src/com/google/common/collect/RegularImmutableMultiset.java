package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true
)
class RegularImmutableMultiset<E> extends ImmutableMultiset<E> {
   private final transient ImmutableMap<E, Integer> map;
   private final transient int size;

   RegularImmutableMultiset(ImmutableMap<E, Integer> map, int size) {
      this.map = map;
      this.size = size;
   }

   boolean isPartialView() {
      return this.map.isPartialView();
   }

   public int count(@Nullable Object element) {
      Integer value = (Integer)this.map.get(element);
      return value == null ? 0 : value;
   }

   public int size() {
      return this.size;
   }

   public boolean contains(@Nullable Object element) {
      return this.map.containsKey(element);
   }

   public ImmutableSet<E> elementSet() {
      return this.map.keySet();
   }

   Multiset.Entry<E> getEntry(int index) {
      java.util.Map.Entry<E, Integer> mapEntry = (java.util.Map.Entry)this.map.entrySet().asList().get(index);
      return Multisets.immutableEntry(mapEntry.getKey(), (Integer)mapEntry.getValue());
   }

   public int hashCode() {
      return this.map.hashCode();
   }
}

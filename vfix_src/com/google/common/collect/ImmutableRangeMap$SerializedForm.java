package com.google.common.collect;

import java.io.Serializable;
import java.util.Map.Entry;

class ImmutableRangeMap$SerializedForm<K extends Comparable<?>, V> implements Serializable {
   private final ImmutableMap<Range<K>, V> mapOfRanges;
   private static final long serialVersionUID = 0L;

   ImmutableRangeMap$SerializedForm(ImmutableMap<Range<K>, V> mapOfRanges) {
      this.mapOfRanges = mapOfRanges;
   }

   Object readResolve() {
      return this.mapOfRanges.isEmpty() ? ImmutableRangeMap.of() : this.createRangeMap();
   }

   Object createRangeMap() {
      ImmutableRangeMap.Builder<K, V> builder = new ImmutableRangeMap.Builder();
      UnmodifiableIterator var2 = this.mapOfRanges.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<Range<K>, V> entry = (Entry)var2.next();
         builder.put((Range)entry.getKey(), entry.getValue());
      }

      return builder.build();
   }
}

package com.google.common.collect;

import java.util.EnumMap;
import java.util.function.BinaryOperator;

class Maps$Accumulator<K extends Enum<K>, V> {
   private final BinaryOperator<V> mergeFunction;
   private EnumMap<K, V> map = null;

   Maps$Accumulator(BinaryOperator<V> mergeFunction) {
      this.mergeFunction = mergeFunction;
   }

   void put(K key, V value) {
      if (this.map == null) {
         this.map = new EnumMap(key.getDeclaringClass());
      }

      this.map.merge(key, value, this.mergeFunction);
   }

   Maps$Accumulator<K, V> combine(Maps$Accumulator<K, V> other) {
      if (this.map == null) {
         return other;
      } else if (other.map == null) {
         return this;
      } else {
         other.map.forEach(this::put);
         return this;
      }
   }

   ImmutableMap<K, V> toImmutableMap() {
      return this.map == null ? ImmutableMap.of() : ImmutableEnumMap.asImmutable(this.map);
   }
}

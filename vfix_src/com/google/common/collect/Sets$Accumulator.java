package com.google.common.collect;

import java.util.EnumSet;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

final class Sets$Accumulator<E extends Enum<E>> {
   static final Collector<Enum<?>, ?, ImmutableSet<? extends Enum<?>>> TO_IMMUTABLE_ENUM_SET;
   private EnumSet<E> set;

   private Sets$Accumulator() {
   }

   void add(E e) {
      if (this.set == null) {
         this.set = EnumSet.of(e);
      } else {
         this.set.add(e);
      }

   }

   Sets$Accumulator<E> combine(Sets$Accumulator<E> other) {
      if (this.set == null) {
         return other;
      } else if (other.set == null) {
         return this;
      } else {
         this.set.addAll(other.set);
         return this;
      }
   }

   ImmutableSet<E> toImmutableSet() {
      return this.set == null ? ImmutableSet.of() : ImmutableEnumSet.asImmutable(this.set);
   }

   static {
      TO_IMMUTABLE_ENUM_SET = Collector.of(Sets$Accumulator::new, Sets$Accumulator::add, Sets$Accumulator::combine, Sets$Accumulator::toImmutableSet, Characteristics.UNORDERED);
   }
}

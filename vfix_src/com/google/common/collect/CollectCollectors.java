package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

@GwtCompatible
final class CollectCollectors {
   private static final Collector<Object, ?, ImmutableList<Object>> TO_IMMUTABLE_LIST = Collector.of(ImmutableList::builder, ImmutableList.Builder::add, ImmutableList.Builder::combine, ImmutableList.Builder::build);
   private static final Collector<Object, ?, ImmutableSet<Object>> TO_IMMUTABLE_SET = Collector.of(ImmutableSet::builder, ImmutableSet.Builder::add, ImmutableSet.Builder::combine, ImmutableSet.Builder::build);
   @GwtIncompatible
   private static final Collector<Range<Comparable>, ?, ImmutableRangeSet<Comparable>> TO_IMMUTABLE_RANGE_SET = Collector.of(ImmutableRangeSet::builder, ImmutableRangeSet.Builder::add, ImmutableRangeSet.Builder::combine, ImmutableRangeSet.Builder::build);

   static <T, K, V> Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
      Preconditions.checkNotNull(keyFunction);
      Preconditions.checkNotNull(valueFunction);
      return Collector.of(ImmutableBiMap.Builder::new, (builder, input) -> {
         builder.put(keyFunction.apply(input), valueFunction.apply(input));
      }, ImmutableBiMap.Builder::combine, ImmutableBiMap.Builder::build);
   }

   static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
      return TO_IMMUTABLE_LIST;
   }

   static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
      Preconditions.checkNotNull(keyFunction);
      Preconditions.checkNotNull(valueFunction);
      return Collector.of(ImmutableMap.Builder::new, (builder, input) -> {
         builder.put(keyFunction.apply(input), valueFunction.apply(input));
      }, ImmutableMap.Builder::combine, ImmutableMap.Builder::build);
   }

   static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
      return TO_IMMUTABLE_SET;
   }

   static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
      Preconditions.checkNotNull(comparator);
      Preconditions.checkNotNull(keyFunction);
      Preconditions.checkNotNull(valueFunction);
      return Collector.of(() -> {
         return new ImmutableSortedMap.Builder(comparator);
      }, (builder, input) -> {
         builder.put(keyFunction.apply(input), valueFunction.apply(input));
      }, ImmutableSortedMap.Builder::combine, ImmutableSortedMap.Builder::build, Characteristics.UNORDERED);
   }

   static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(Comparator<? super E> comparator) {
      Preconditions.checkNotNull(comparator);
      return Collector.of(() -> {
         return new ImmutableSortedSet.Builder(comparator);
      }, ImmutableSortedSet.Builder::add, ImmutableSortedSet.Builder::combine, ImmutableSortedSet.Builder::build);
   }

   @GwtIncompatible
   static <E extends Comparable<? super E>> Collector<Range<E>, ?, ImmutableRangeSet<E>> toImmutableRangeSet() {
      return TO_IMMUTABLE_RANGE_SET;
   }

   @GwtIncompatible
   static <T, K extends Comparable<? super K>, V> Collector<T, ?, ImmutableRangeMap<K, V>> toImmutableRangeMap(Function<? super T, Range<K>> keyFunction, Function<? super T, ? extends V> valueFunction) {
      Preconditions.checkNotNull(keyFunction);
      Preconditions.checkNotNull(valueFunction);
      return Collector.of(ImmutableRangeMap::builder, (builder, input) -> {
         builder.put((Range)keyFunction.apply(input), valueFunction.apply(input));
      }, ImmutableRangeMap.Builder::combine, ImmutableRangeMap.Builder::build);
   }
}

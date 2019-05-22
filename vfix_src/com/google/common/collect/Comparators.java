package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

@Beta
@GwtCompatible
public final class Comparators {
   private Comparators() {
   }

   public static <T, S extends T> Comparator<Iterable<S>> lexicographical(Comparator<T> comparator) {
      return new LexicographicalOrdering((Comparator)Preconditions.checkNotNull(comparator));
   }

   public static <T> boolean isInOrder(Iterable<? extends T> iterable, Comparator<T> comparator) {
      Preconditions.checkNotNull(comparator);
      Iterator<? extends T> it = iterable.iterator();
      Object next;
      if (it.hasNext()) {
         for(Object prev = it.next(); it.hasNext(); prev = next) {
            next = it.next();
            if (comparator.compare(prev, next) > 0) {
               return false;
            }
         }
      }

      return true;
   }

   public static <T> boolean isInStrictOrder(Iterable<? extends T> iterable, Comparator<T> comparator) {
      Preconditions.checkNotNull(comparator);
      Iterator<? extends T> it = iterable.iterator();
      Object next;
      if (it.hasNext()) {
         for(Object prev = it.next(); it.hasNext(); prev = next) {
            next = it.next();
            if (comparator.compare(prev, next) >= 0) {
               return false;
            }
         }
      }

      return true;
   }

   public static <T> Collector<T, ?, List<T>> least(int k, Comparator<? super T> comparator) {
      CollectPreconditions.checkNonnegative(k, "k");
      Preconditions.checkNotNull(comparator);
      return Collector.of(() -> {
         return TopKSelector.least(k, comparator);
      }, TopKSelector::offer, TopKSelector::combine, TopKSelector::topK, Characteristics.UNORDERED);
   }

   public static <T> Collector<T, ?, List<T>> greatest(int k, Comparator<? super T> comparator) {
      return least(k, comparator.reversed());
   }

   @Beta
   public static <T> Comparator<Optional<T>> emptiesFirst(Comparator<T> valueComparator) {
      Preconditions.checkNotNull(valueComparator);
      return Comparator.comparing((o) -> {
         return o.orElse((Object)null);
      }, Comparator.nullsFirst(valueComparator));
   }

   @Beta
   public static <T> Comparator<Optional<T>> emptiesLast(Comparator<T> valueComparator) {
      Preconditions.checkNotNull(valueComparator);
      return Comparator.comparing((o) -> {
         return o.orElse((Object)null);
      }, Comparator.nullsLast(valueComparator));
   }
}

package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
final class SortedMultisets {
   private SortedMultisets() {
   }

   private static <E> E getElementOrThrow(Multiset.Entry<E> entry) {
      if (entry == null) {
         throw new NoSuchElementException();
      } else {
         return entry.getElement();
      }
   }

   private static <E> E getElementOrNull(@Nullable Multiset.Entry<E> entry) {
      return entry == null ? null : entry.getElement();
   }

   @GwtIncompatible("Navigable")
   static class NavigableElementSet<E> extends SortedMultisets.ElementSet<E> implements NavigableSet<E> {
      NavigableElementSet(SortedMultiset<E> multiset) {
         super(multiset);
      }

      public E lower(E e) {
         return SortedMultisets.getElementOrNull(this.multiset().headMultiset(e, BoundType.OPEN).lastEntry());
      }

      public E floor(E e) {
         return SortedMultisets.getElementOrNull(this.multiset().headMultiset(e, BoundType.CLOSED).lastEntry());
      }

      public E ceiling(E e) {
         return SortedMultisets.getElementOrNull(this.multiset().tailMultiset(e, BoundType.CLOSED).firstEntry());
      }

      public E higher(E e) {
         return SortedMultisets.getElementOrNull(this.multiset().tailMultiset(e, BoundType.OPEN).firstEntry());
      }

      public NavigableSet<E> descendingSet() {
         return new SortedMultisets.NavigableElementSet(this.multiset().descendingMultiset());
      }

      public Iterator<E> descendingIterator() {
         return this.descendingSet().iterator();
      }

      public E pollFirst() {
         return SortedMultisets.getElementOrNull(this.multiset().pollFirstEntry());
      }

      public E pollLast() {
         return SortedMultisets.getElementOrNull(this.multiset().pollLastEntry());
      }

      public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
         return new SortedMultisets.NavigableElementSet(this.multiset().subMultiset(fromElement, BoundType.forBoolean(fromInclusive), toElement, BoundType.forBoolean(toInclusive)));
      }

      public NavigableSet<E> headSet(E toElement, boolean inclusive) {
         return new SortedMultisets.NavigableElementSet(this.multiset().headMultiset(toElement, BoundType.forBoolean(inclusive)));
      }

      public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
         return new SortedMultisets.NavigableElementSet(this.multiset().tailMultiset(fromElement, BoundType.forBoolean(inclusive)));
      }
   }

   static class ElementSet<E> extends Multisets.ElementSet<E> implements SortedSet<E> {
      private final SortedMultiset<E> multiset;

      ElementSet(SortedMultiset<E> multiset) {
         this.multiset = multiset;
      }

      final SortedMultiset<E> multiset() {
         return this.multiset;
      }

      public Comparator<? super E> comparator() {
         return this.multiset().comparator();
      }

      public SortedSet<E> subSet(E fromElement, E toElement) {
         return this.multiset().subMultiset(fromElement, BoundType.CLOSED, toElement, BoundType.OPEN).elementSet();
      }

      public SortedSet<E> headSet(E toElement) {
         return this.multiset().headMultiset(toElement, BoundType.OPEN).elementSet();
      }

      public SortedSet<E> tailSet(E fromElement) {
         return this.multiset().tailMultiset(fromElement, BoundType.CLOSED).elementSet();
      }

      public E first() {
         return SortedMultisets.getElementOrThrow(this.multiset().firstEntry());
      }

      public E last() {
         return SortedMultisets.getElementOrThrow(this.multiset().lastEntry());
      }
   }
}

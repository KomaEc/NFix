package org.jf.util;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import javax.annotation.Nonnull;

public class CollectionUtils {
   public static <T> int listHashCode(@Nonnull Iterable<T> iterable) {
      int hashCode = 1;

      Object item;
      for(Iterator var2 = iterable.iterator(); var2.hasNext(); hashCode = hashCode * 31 + item.hashCode()) {
         item = var2.next();
      }

      return hashCode;
   }

   public static <T> int lastIndexOf(@Nonnull Iterable<T> iterable, @Nonnull Predicate<? super T> predicate) {
      int index = 0;
      int lastMatchingIndex = -1;

      for(Iterator var4 = iterable.iterator(); var4.hasNext(); ++index) {
         T item = var4.next();
         if (predicate.apply(item)) {
            lastMatchingIndex = index;
         }
      }

      return lastMatchingIndex;
   }

   public static <T extends Comparable<? super T>> int compareAsList(@Nonnull Collection<? extends T> list1, @Nonnull Collection<? extends T> list2) {
      int res = Ints.compare(list1.size(), list2.size());
      if (res != 0) {
         return res;
      } else {
         Iterator<? extends T> elements2 = list2.iterator();
         Iterator var4 = list1.iterator();

         do {
            if (!var4.hasNext()) {
               return 0;
            }

            T element1 = (Comparable)var4.next();
            res = element1.compareTo(elements2.next());
         } while(res == 0);

         return res;
      }
   }

   public static <T> int compareAsIterable(@Nonnull Comparator<? super T> comparator, @Nonnull Iterable<? extends T> it1, @Nonnull Iterable<? extends T> it2) {
      Iterator<? extends T> elements2 = it2.iterator();
      Iterator var4 = it1.iterator();

      while(var4.hasNext()) {
         Object element1 = var4.next();

         Object element2;
         try {
            element2 = elements2.next();
         } catch (NoSuchElementException var8) {
            return 1;
         }

         int res = comparator.compare(element1, element2);
         if (res != 0) {
            return res;
         }
      }

      return elements2.hasNext() ? -1 : 0;
   }

   public static <T extends Comparable<? super T>> int compareAsIterable(@Nonnull Iterable<? extends T> it1, @Nonnull Iterable<? extends T> it2) {
      Iterator<? extends T> elements2 = it2.iterator();
      Iterator var3 = it1.iterator();

      while(var3.hasNext()) {
         Comparable element1 = (Comparable)var3.next();

         Comparable element2;
         try {
            element2 = (Comparable)elements2.next();
         } catch (NoSuchElementException var7) {
            return 1;
         }

         int res = element1.compareTo(element2);
         if (res != 0) {
            return res;
         }
      }

      return elements2.hasNext() ? -1 : 0;
   }

   public static <T> int compareAsList(@Nonnull Comparator<? super T> elementComparator, @Nonnull Collection<? extends T> list1, @Nonnull Collection<? extends T> list2) {
      int res = Ints.compare(list1.size(), list2.size());
      if (res != 0) {
         return res;
      } else {
         Iterator<? extends T> elements2 = list2.iterator();
         Iterator var5 = list1.iterator();

         do {
            if (!var5.hasNext()) {
               return 0;
            }

            T element1 = var5.next();
            res = elementComparator.compare(element1, elements2.next());
         } while(res == 0);

         return res;
      }
   }

   @Nonnull
   public static <T> Comparator<Collection<? extends T>> listComparator(@Nonnull final Comparator<? super T> elementComparator) {
      return new Comparator<Collection<? extends T>>() {
         public int compare(Collection<? extends T> list1, Collection<? extends T> list2) {
            return CollectionUtils.compareAsList(elementComparator, list1, list2);
         }
      };
   }

   public static <T> boolean isNaturalSortedSet(@Nonnull Iterable<? extends T> it) {
      if (!(it instanceof SortedSet)) {
         return false;
      } else {
         SortedSet<? extends T> sortedSet = (SortedSet)it;
         Comparator<?> comparator = sortedSet.comparator();
         return comparator == null || comparator.equals(Ordering.natural());
      }
   }

   public static <T> boolean isSortedSet(@Nonnull Comparator<? extends T> elementComparator, @Nonnull Iterable<? extends T> it) {
      if (it instanceof SortedSet) {
         SortedSet<? extends T> sortedSet = (SortedSet)it;
         Comparator<?> comparator = sortedSet.comparator();
         return comparator == null ? elementComparator.equals(Ordering.natural()) : elementComparator.equals(comparator);
      } else {
         return false;
      }
   }

   @Nonnull
   private static <T> SortedSet<? extends T> toNaturalSortedSet(@Nonnull Collection<? extends T> collection) {
      return (SortedSet)(isNaturalSortedSet(collection) ? (SortedSet)collection : ImmutableSortedSet.copyOf(collection));
   }

   @Nonnull
   private static <T> SortedSet<? extends T> toSortedSet(@Nonnull Comparator<? super T> elementComparator, @Nonnull Collection<? extends T> collection) {
      if (collection instanceof SortedSet) {
         SortedSet<? extends T> sortedSet = (SortedSet)collection;
         Comparator<?> comparator = sortedSet.comparator();
         if (comparator != null && comparator.equals(elementComparator)) {
            return sortedSet;
         }
      }

      return ImmutableSortedSet.copyOf(elementComparator, collection);
   }

   @Nonnull
   public static <T> Comparator<Collection<? extends T>> setComparator(@Nonnull final Comparator<? super T> elementComparator) {
      return new Comparator<Collection<? extends T>>() {
         public int compare(Collection<? extends T> list1, Collection<? extends T> list2) {
            return CollectionUtils.compareAsSet(elementComparator, list1, list2);
         }
      };
   }

   public static <T extends Comparable<T>> int compareAsSet(@Nonnull Collection<? extends T> set1, @Nonnull Collection<? extends T> set2) {
      int res = Ints.compare(set1.size(), set2.size());
      if (res != 0) {
         return res;
      } else {
         SortedSet<? extends T> sortedSet1 = toNaturalSortedSet(set1);
         SortedSet<? extends T> sortedSet2 = toNaturalSortedSet(set2);
         Iterator<? extends T> elements2 = set2.iterator();
         Iterator var6 = set1.iterator();

         do {
            if (!var6.hasNext()) {
               return 0;
            }

            T element1 = (Comparable)var6.next();
            res = element1.compareTo(elements2.next());
         } while(res == 0);

         return res;
      }
   }

   public static <T> int compareAsSet(@Nonnull Comparator<? super T> elementComparator, @Nonnull Collection<? extends T> list1, @Nonnull Collection<? extends T> list2) {
      int res = Ints.compare(list1.size(), list2.size());
      if (res != 0) {
         return res;
      } else {
         SortedSet<? extends T> set1 = toSortedSet(elementComparator, list1);
         SortedSet<? extends T> set2 = toSortedSet(elementComparator, list2);
         Iterator<? extends T> elements2 = set2.iterator();
         Iterator var7 = set1.iterator();

         do {
            if (!var7.hasNext()) {
               return 0;
            }

            T element1 = var7.next();
            res = elementComparator.compare(element1, elements2.next());
         } while(res == 0);

         return res;
      }
   }
}

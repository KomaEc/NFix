package org.jf.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ImmutableConverter<ImmutableItem, Item> {
   protected abstract boolean isImmutable(@Nonnull Item var1);

   @Nonnull
   protected abstract ImmutableItem makeImmutable(@Nonnull Item var1);

   @Nonnull
   public ImmutableList<ImmutableItem> toList(@Nullable Iterable<? extends Item> iterable) {
      if (iterable == null) {
         return ImmutableList.of();
      } else {
         boolean needsCopy = false;
         final Iterator iter;
         if (iterable instanceof ImmutableList) {
            iter = iterable.iterator();

            while(iter.hasNext()) {
               Item element = iter.next();
               if (!this.isImmutable(element)) {
                  needsCopy = true;
                  break;
               }
            }
         } else {
            needsCopy = true;
         }

         if (!needsCopy) {
            return (ImmutableList)iterable;
         } else {
            iter = iterable.iterator();
            return ImmutableList.copyOf(new Iterator<ImmutableItem>() {
               public boolean hasNext() {
                  return iter.hasNext();
               }

               public ImmutableItem next() {
                  return ImmutableConverter.this.makeImmutable(iter.next());
               }

               public void remove() {
                  iter.remove();
               }
            });
         }
      }
   }

   @Nonnull
   public ImmutableSet<ImmutableItem> toSet(@Nullable Iterable<? extends Item> iterable) {
      if (iterable == null) {
         return ImmutableSet.of();
      } else {
         boolean needsCopy = false;
         final Iterator iter;
         if (iterable instanceof ImmutableSet) {
            iter = iterable.iterator();

            while(iter.hasNext()) {
               Item element = iter.next();
               if (!this.isImmutable(element)) {
                  needsCopy = true;
                  break;
               }
            }
         } else {
            needsCopy = true;
         }

         if (!needsCopy) {
            return (ImmutableSet)iterable;
         } else {
            iter = iterable.iterator();
            return ImmutableSet.copyOf(new Iterator<ImmutableItem>() {
               public boolean hasNext() {
                  return iter.hasNext();
               }

               public ImmutableItem next() {
                  return ImmutableConverter.this.makeImmutable(iter.next());
               }

               public void remove() {
                  iter.remove();
               }
            });
         }
      }
   }

   @Nonnull
   public ImmutableSortedSet<ImmutableItem> toSortedSet(@Nonnull Comparator<? super ImmutableItem> comparator, @Nullable Iterable<? extends Item> iterable) {
      if (iterable == null) {
         return ImmutableSortedSet.of();
      } else {
         boolean needsCopy = false;
         final Iterator iter;
         if (iterable instanceof ImmutableSortedSet && ((ImmutableSortedSet)iterable).comparator().equals(comparator)) {
            iter = iterable.iterator();

            while(iter.hasNext()) {
               Item element = iter.next();
               if (!this.isImmutable(element)) {
                  needsCopy = true;
                  break;
               }
            }
         } else {
            needsCopy = true;
         }

         if (!needsCopy) {
            return (ImmutableSortedSet)iterable;
         } else {
            iter = iterable.iterator();
            return ImmutableSortedSet.copyOf(comparator, new Iterator<ImmutableItem>() {
               public boolean hasNext() {
                  return iter.hasNext();
               }

               public ImmutableItem next() {
                  return ImmutableConverter.this.makeImmutable(iter.next());
               }

               public void remove() {
                  iter.remove();
               }
            });
         }
      }
   }

   @Nonnull
   public SortedSet<ImmutableItem> toSortedSet(@Nonnull Comparator<? super ImmutableItem> comparator, @Nullable SortedSet<? extends Item> sortedSet) {
      if (sortedSet != null && sortedSet.size() != 0) {
         ImmutableItem[] newItems = (Object[])(new Object[sortedSet.size()]);
         int index = 0;

         Object item;
         for(Iterator var5 = sortedSet.iterator(); var5.hasNext(); newItems[index++] = this.makeImmutable(item)) {
            item = var5.next();
         }

         return ArraySortedSet.of(comparator, newItems);
      } else {
         return ImmutableSortedSet.of();
      }
   }
}

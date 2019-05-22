package org.jf.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ImmutableUtils {
   @Nonnull
   public static <T> ImmutableList<T> nullToEmptyList(@Nullable ImmutableList<T> list) {
      return list == null ? ImmutableList.of() : list;
   }

   @Nonnull
   public static <T> ImmutableSet<T> nullToEmptySet(@Nullable ImmutableSet<T> set) {
      return set == null ? ImmutableSet.of() : set;
   }

   @Nonnull
   public static <T> ImmutableSortedSet<T> nullToEmptySortedSet(@Nullable ImmutableSortedSet<T> set) {
      return set == null ? ImmutableSortedSet.of() : set;
   }
}

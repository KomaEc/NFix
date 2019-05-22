package org.jf.dexlib2.immutable.util;

import com.google.common.collect.ImmutableList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.util.ImmutableConverter;

public final class CharSequenceConverter {
   private static final ImmutableConverter<String, CharSequence> CONVERTER = new ImmutableConverter<String, CharSequence>() {
      protected boolean isImmutable(@Nonnull CharSequence item) {
         return item instanceof String;
      }

      @Nonnull
      protected String makeImmutable(@Nonnull CharSequence item) {
         return item.toString();
      }
   };

   private CharSequenceConverter() {
   }

   @Nonnull
   public static ImmutableList<String> immutableStringList(@Nullable Iterable<? extends CharSequence> iterable) {
      return CONVERTER.toList(iterable);
   }
}

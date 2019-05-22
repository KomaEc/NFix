package org.jf.dexlib2.writer.builder;

import javax.annotation.Nonnull;

public class BaseBuilderPool {
   @Nonnull
   protected final DexBuilder dexBuilder;

   public BaseBuilderPool(@Nonnull DexBuilder dexBuilder) {
      this.dexBuilder = dexBuilder;
   }
}

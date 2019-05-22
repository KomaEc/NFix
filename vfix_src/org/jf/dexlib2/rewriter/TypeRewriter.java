package org.jf.dexlib2.rewriter;

import javax.annotation.Nonnull;

public class TypeRewriter implements Rewriter<String> {
   @Nonnull
   public String rewrite(@Nonnull String value) {
      return value;
   }
}

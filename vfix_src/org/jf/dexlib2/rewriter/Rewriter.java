package org.jf.dexlib2.rewriter;

import javax.annotation.Nonnull;

public interface Rewriter<T> {
   @Nonnull
   T rewrite(@Nonnull T var1);
}

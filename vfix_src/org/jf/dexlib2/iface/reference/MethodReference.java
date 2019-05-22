package org.jf.dexlib2.iface.reference;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface MethodReference extends Reference, Comparable<MethodReference> {
   @Nonnull
   String getDefiningClass();

   @Nonnull
   String getName();

   @Nonnull
   List<? extends CharSequence> getParameterTypes();

   @Nonnull
   String getReturnType();

   int hashCode();

   boolean equals(@Nullable Object var1);

   int compareTo(@Nonnull MethodReference var1);
}

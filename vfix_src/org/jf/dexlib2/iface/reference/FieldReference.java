package org.jf.dexlib2.iface.reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface FieldReference extends Reference, Comparable<FieldReference> {
   @Nonnull
   String getDefiningClass();

   @Nonnull
   String getName();

   @Nonnull
   String getType();

   int hashCode();

   boolean equals(@Nullable Object var1);

   int compareTo(@Nonnull FieldReference var1);
}

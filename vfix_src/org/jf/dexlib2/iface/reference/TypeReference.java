package org.jf.dexlib2.iface.reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface TypeReference extends Reference, CharSequence, Comparable<CharSequence> {
   @Nonnull
   String getType();

   int hashCode();

   boolean equals(@Nullable Object var1);

   int compareTo(@Nonnull CharSequence var1);
}

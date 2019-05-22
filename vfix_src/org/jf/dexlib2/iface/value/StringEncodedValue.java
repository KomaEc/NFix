package org.jf.dexlib2.iface.value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface StringEncodedValue extends EncodedValue {
   @Nonnull
   String getValue();

   int hashCode();

   boolean equals(@Nullable Object var1);

   int compareTo(@Nonnull EncodedValue var1);
}

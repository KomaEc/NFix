package org.jf.dexlib2.iface.value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface BooleanEncodedValue extends EncodedValue {
   boolean getValue();

   int hashCode();

   boolean equals(@Nullable Object var1);

   int compareTo(@Nonnull EncodedValue var1);
}

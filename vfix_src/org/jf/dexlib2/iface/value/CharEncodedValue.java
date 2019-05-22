package org.jf.dexlib2.iface.value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface CharEncodedValue extends EncodedValue {
   char getValue();

   int hashCode();

   boolean equals(@Nullable Object var1);

   int compareTo(@Nonnull EncodedValue var1);
}

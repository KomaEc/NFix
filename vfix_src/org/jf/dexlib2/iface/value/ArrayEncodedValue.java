package org.jf.dexlib2.iface.value;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ArrayEncodedValue extends EncodedValue {
   @Nonnull
   List<? extends EncodedValue> getValue();

   int hashCode();

   boolean equals(@Nullable Object var1);

   int compareTo(@Nonnull EncodedValue var1);
}

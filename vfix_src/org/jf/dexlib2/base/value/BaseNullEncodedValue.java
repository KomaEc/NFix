package org.jf.dexlib2.base.value;

import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.NullEncodedValue;

public abstract class BaseNullEncodedValue implements NullEncodedValue {
   public int hashCode() {
      return 0;
   }

   public boolean equals(@Nullable Object o) {
      return o instanceof NullEncodedValue;
   }

   public int compareTo(@Nonnull EncodedValue o) {
      return Ints.compare(this.getValueType(), o.getValueType());
   }

   public int getValueType() {
      return 30;
   }
}

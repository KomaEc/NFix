package org.jf.dexlib2.base.value;

import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.IntEncodedValue;

public abstract class BaseIntEncodedValue implements IntEncodedValue {
   public int hashCode() {
      return this.getValue();
   }

   public boolean equals(@Nullable Object o) {
      if (o instanceof IntEncodedValue) {
         return this.getValue() == ((IntEncodedValue)o).getValue();
      } else {
         return false;
      }
   }

   public int compareTo(@Nonnull EncodedValue o) {
      int res = Ints.compare(this.getValueType(), o.getValueType());
      return res != 0 ? res : Ints.compare(this.getValue(), ((IntEncodedValue)o).getValue());
   }

   public int getValueType() {
      return 4;
   }
}

package org.jf.dexlib2.base.value;

import com.google.common.primitives.Booleans;
import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.value.BooleanEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;

public abstract class BaseBooleanEncodedValue implements BooleanEncodedValue {
   public int hashCode() {
      return this.getValue() ? 1 : 0;
   }

   public boolean equals(@Nullable Object o) {
      if (o instanceof BooleanEncodedValue) {
         return this.getValue() == ((BooleanEncodedValue)o).getValue();
      } else {
         return false;
      }
   }

   public int compareTo(@Nonnull EncodedValue o) {
      int res = Ints.compare(this.getValueType(), o.getValueType());
      return res != 0 ? res : Booleans.compare(this.getValue(), ((BooleanEncodedValue)o).getValue());
   }

   public int getValueType() {
      return 31;
   }
}

package org.jf.dexlib2.base.value;

import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.FloatEncodedValue;

public abstract class BaseFloatEncodedValue implements FloatEncodedValue {
   public int hashCode() {
      return Float.floatToRawIntBits(this.getValue());
   }

   public boolean equals(@Nullable Object o) {
      if (o != null && o instanceof FloatEncodedValue) {
         return Float.floatToRawIntBits(this.getValue()) == Float.floatToRawIntBits(((FloatEncodedValue)o).getValue());
      } else {
         return false;
      }
   }

   public int compareTo(@Nonnull EncodedValue o) {
      int res = Ints.compare(this.getValueType(), o.getValueType());
      return res != 0 ? res : Float.compare(this.getValue(), ((FloatEncodedValue)o).getValue());
   }

   public int getValueType() {
      return 16;
   }
}

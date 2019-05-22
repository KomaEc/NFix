package org.jf.dexlib2.base.value;

import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.value.DoubleEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;

public abstract class BaseDoubleEncodedValue implements DoubleEncodedValue {
   public int hashCode() {
      long v = Double.doubleToRawLongBits(this.getValue());
      return (int)(v ^ v >>> 32);
   }

   public boolean equals(@Nullable Object o) {
      if (o instanceof DoubleEncodedValue) {
         return Double.doubleToRawLongBits(this.getValue()) == Double.doubleToRawLongBits(((DoubleEncodedValue)o).getValue());
      } else {
         return false;
      }
   }

   public int compareTo(@Nonnull EncodedValue o) {
      int res = Ints.compare(this.getValueType(), o.getValueType());
      return res != 0 ? res : Double.compare(this.getValue(), ((DoubleEncodedValue)o).getValue());
   }

   public int getValueType() {
      return 17;
   }
}

package org.jf.dexlib2.base.value;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.LongEncodedValue;

public abstract class BaseLongEncodedValue implements LongEncodedValue {
   public int hashCode() {
      long value = this.getValue();
      int hashCode = (int)value;
      return hashCode * 31 + (int)(value >>> 32);
   }

   public boolean equals(@Nullable Object o) {
      if (o instanceof LongEncodedValue) {
         return this.getValue() == ((LongEncodedValue)o).getValue();
      } else {
         return false;
      }
   }

   public int compareTo(@Nonnull EncodedValue o) {
      int res = Ints.compare(this.getValueType(), o.getValueType());
      return res != 0 ? res : Longs.compare(this.getValue(), ((LongEncodedValue)o).getValue());
   }

   public int getValueType() {
      return 6;
   }
}

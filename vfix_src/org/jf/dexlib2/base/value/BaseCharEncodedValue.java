package org.jf.dexlib2.base.value;

import com.google.common.primitives.Chars;
import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.value.CharEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;

public abstract class BaseCharEncodedValue implements CharEncodedValue {
   public int hashCode() {
      return this.getValue();
   }

   public boolean equals(@Nullable Object o) {
      if (o instanceof CharEncodedValue) {
         return this.getValue() == ((CharEncodedValue)o).getValue();
      } else {
         return false;
      }
   }

   public int compareTo(@Nonnull EncodedValue o) {
      int res = Ints.compare(this.getValueType(), o.getValueType());
      return res != 0 ? res : Chars.compare(this.getValue(), ((CharEncodedValue)o).getValue());
   }

   public int getValueType() {
      return 3;
   }
}

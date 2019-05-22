package org.jf.dexlib2.base.value;

import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.value.ByteEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;

public abstract class BaseByteEncodedValue implements ByteEncodedValue {
   public int hashCode() {
      return this.getValue();
   }

   public boolean equals(@Nullable Object o) {
      if (o instanceof ByteEncodedValue) {
         return this.getValue() == ((ByteEncodedValue)o).getValue();
      } else {
         return false;
      }
   }

   public int compareTo(@Nonnull EncodedValue o) {
      int res = Ints.compare(this.getValueType(), o.getValueType());
      return res != 0 ? res : Ints.compare(this.getValue(), ((ByteEncodedValue)o).getValue());
   }

   public int getValueType() {
      return 0;
   }
}

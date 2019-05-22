package org.jf.dexlib2.base.value;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Shorts;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.ShortEncodedValue;

public abstract class BaseShortEncodedValue implements ShortEncodedValue {
   public int hashCode() {
      return this.getValue();
   }

   public boolean equals(@Nullable Object o) {
      if (o instanceof ShortEncodedValue) {
         return this.getValue() == ((ShortEncodedValue)o).getValue();
      } else {
         return false;
      }
   }

   public int compareTo(@Nonnull EncodedValue o) {
      int res = Ints.compare(this.getValueType(), o.getValueType());
      return res != 0 ? res : Shorts.compare(this.getValue(), ((ShortEncodedValue)o).getValue());
   }

   public int getValueType() {
      return 2;
   }
}

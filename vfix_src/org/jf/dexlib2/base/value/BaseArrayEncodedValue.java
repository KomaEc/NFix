package org.jf.dexlib2.base.value;

import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.value.ArrayEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.util.CollectionUtils;

public abstract class BaseArrayEncodedValue implements ArrayEncodedValue {
   public int hashCode() {
      return this.getValue().hashCode();
   }

   public boolean equals(@Nullable Object o) {
      return o instanceof ArrayEncodedValue ? this.getValue().equals(((ArrayEncodedValue)o).getValue()) : false;
   }

   public int compareTo(@Nonnull EncodedValue o) {
      int res = Ints.compare(this.getValueType(), o.getValueType());
      return res != 0 ? res : CollectionUtils.compareAsList(this.getValue(), ((ArrayEncodedValue)o).getValue());
   }

   public int getValueType() {
      return 28;
   }
}

package org.jf.dexlib2.base.value;

import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.MethodEncodedValue;

public abstract class BaseMethodEncodedValue implements MethodEncodedValue {
   public int hashCode() {
      return this.getValue().hashCode();
   }

   public boolean equals(@Nullable Object o) {
      return o instanceof MethodEncodedValue ? this.getValue().equals(((MethodEncodedValue)o).getValue()) : false;
   }

   public int compareTo(@Nonnull EncodedValue o) {
      int res = Ints.compare(this.getValueType(), o.getValueType());
      return res != 0 ? res : this.getValue().compareTo(((MethodEncodedValue)o).getValue());
   }

   public int getValueType() {
      return 26;
   }
}

package org.jf.dexlib2.base.value;

import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.TypeEncodedValue;

public abstract class BaseTypeEncodedValue implements TypeEncodedValue {
   public int hashCode() {
      return this.getValue().hashCode();
   }

   public boolean equals(@Nullable Object o) {
      return o instanceof TypeEncodedValue ? this.getValue().equals(((TypeEncodedValue)o).getValue()) : false;
   }

   public int compareTo(@Nonnull EncodedValue o) {
      int res = Ints.compare(this.getValueType(), o.getValueType());
      return res != 0 ? res : this.getValue().compareTo(((TypeEncodedValue)o).getValue());
   }

   public int getValueType() {
      return 24;
   }
}

package org.jf.dexlib2.immutable.value;

import org.jf.dexlib2.base.value.BaseIntEncodedValue;
import org.jf.dexlib2.iface.value.IntEncodedValue;

public class ImmutableIntEncodedValue extends BaseIntEncodedValue implements ImmutableEncodedValue {
   protected final int value;

   public ImmutableIntEncodedValue(int value) {
      this.value = value;
   }

   public static ImmutableIntEncodedValue of(IntEncodedValue intEncodedValue) {
      return intEncodedValue instanceof ImmutableIntEncodedValue ? (ImmutableIntEncodedValue)intEncodedValue : new ImmutableIntEncodedValue(intEncodedValue.getValue());
   }

   public int getValue() {
      return this.value;
   }
}

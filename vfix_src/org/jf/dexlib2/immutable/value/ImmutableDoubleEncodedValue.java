package org.jf.dexlib2.immutable.value;

import org.jf.dexlib2.base.value.BaseDoubleEncodedValue;
import org.jf.dexlib2.iface.value.DoubleEncodedValue;

public class ImmutableDoubleEncodedValue extends BaseDoubleEncodedValue implements ImmutableEncodedValue {
   protected final double value;

   public ImmutableDoubleEncodedValue(double value) {
      this.value = value;
   }

   public static ImmutableDoubleEncodedValue of(DoubleEncodedValue doubleEncodedValue) {
      return doubleEncodedValue instanceof ImmutableDoubleEncodedValue ? (ImmutableDoubleEncodedValue)doubleEncodedValue : new ImmutableDoubleEncodedValue(doubleEncodedValue.getValue());
   }

   public double getValue() {
      return this.value;
   }
}

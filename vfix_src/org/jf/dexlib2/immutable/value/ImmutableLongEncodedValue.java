package org.jf.dexlib2.immutable.value;

import org.jf.dexlib2.base.value.BaseLongEncodedValue;
import org.jf.dexlib2.iface.value.LongEncodedValue;

public class ImmutableLongEncodedValue extends BaseLongEncodedValue implements ImmutableEncodedValue {
   protected final long value;

   public ImmutableLongEncodedValue(long value) {
      this.value = value;
   }

   public static ImmutableLongEncodedValue of(LongEncodedValue longEncodedValue) {
      return longEncodedValue instanceof ImmutableLongEncodedValue ? (ImmutableLongEncodedValue)longEncodedValue : new ImmutableLongEncodedValue(longEncodedValue.getValue());
   }

   public long getValue() {
      return this.value;
   }
}

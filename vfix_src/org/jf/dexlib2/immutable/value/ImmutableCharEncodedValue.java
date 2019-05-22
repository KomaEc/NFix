package org.jf.dexlib2.immutable.value;

import org.jf.dexlib2.base.value.BaseCharEncodedValue;
import org.jf.dexlib2.iface.value.CharEncodedValue;

public class ImmutableCharEncodedValue extends BaseCharEncodedValue implements ImmutableEncodedValue {
   protected final char value;

   public ImmutableCharEncodedValue(char value) {
      this.value = value;
   }

   public static ImmutableCharEncodedValue of(CharEncodedValue charEncodedValue) {
      return charEncodedValue instanceof ImmutableCharEncodedValue ? (ImmutableCharEncodedValue)charEncodedValue : new ImmutableCharEncodedValue(charEncodedValue.getValue());
   }

   public char getValue() {
      return this.value;
   }
}

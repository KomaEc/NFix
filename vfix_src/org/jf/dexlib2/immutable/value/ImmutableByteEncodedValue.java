package org.jf.dexlib2.immutable.value;

import org.jf.dexlib2.base.value.BaseByteEncodedValue;
import org.jf.dexlib2.iface.value.ByteEncodedValue;

public class ImmutableByteEncodedValue extends BaseByteEncodedValue implements ImmutableEncodedValue {
   protected final byte value;

   public ImmutableByteEncodedValue(byte value) {
      this.value = value;
   }

   public static ImmutableByteEncodedValue of(ByteEncodedValue byteEncodedValue) {
      return byteEncodedValue instanceof ImmutableByteEncodedValue ? (ImmutableByteEncodedValue)byteEncodedValue : new ImmutableByteEncodedValue(byteEncodedValue.getValue());
   }

   public byte getValue() {
      return this.value;
   }
}

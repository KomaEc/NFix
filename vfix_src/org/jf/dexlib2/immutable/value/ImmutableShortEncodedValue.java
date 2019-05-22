package org.jf.dexlib2.immutable.value;

import org.jf.dexlib2.base.value.BaseShortEncodedValue;
import org.jf.dexlib2.iface.value.ShortEncodedValue;

public class ImmutableShortEncodedValue extends BaseShortEncodedValue implements ImmutableEncodedValue {
   protected final short value;

   public ImmutableShortEncodedValue(short value) {
      this.value = value;
   }

   public static ImmutableShortEncodedValue of(ShortEncodedValue shortEncodedValue) {
      return shortEncodedValue instanceof ImmutableShortEncodedValue ? (ImmutableShortEncodedValue)shortEncodedValue : new ImmutableShortEncodedValue(shortEncodedValue.getValue());
   }

   public short getValue() {
      return this.value;
   }
}

package org.jf.dexlib2.immutable.value;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseStringEncodedValue;
import org.jf.dexlib2.iface.value.StringEncodedValue;

public class ImmutableStringEncodedValue extends BaseStringEncodedValue implements ImmutableEncodedValue {
   @Nonnull
   protected final String value;

   public ImmutableStringEncodedValue(@Nonnull String value) {
      this.value = value;
   }

   public static ImmutableStringEncodedValue of(@Nonnull StringEncodedValue stringEncodedValue) {
      return stringEncodedValue instanceof ImmutableStringEncodedValue ? (ImmutableStringEncodedValue)stringEncodedValue : new ImmutableStringEncodedValue(stringEncodedValue.getValue());
   }

   @Nonnull
   public String getValue() {
      return this.value;
   }
}

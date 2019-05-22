package org.jf.dexlib2.immutable.value;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseFieldEncodedValue;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.value.FieldEncodedValue;

public class ImmutableFieldEncodedValue extends BaseFieldEncodedValue implements ImmutableEncodedValue {
   @Nonnull
   protected final FieldReference value;

   public ImmutableFieldEncodedValue(@Nonnull FieldReference value) {
      this.value = value;
   }

   public static ImmutableFieldEncodedValue of(@Nonnull FieldEncodedValue fieldEncodedValue) {
      return fieldEncodedValue instanceof ImmutableFieldEncodedValue ? (ImmutableFieldEncodedValue)fieldEncodedValue : new ImmutableFieldEncodedValue(fieldEncodedValue.getValue());
   }

   @Nonnull
   public FieldReference getValue() {
      return this.value;
   }
}

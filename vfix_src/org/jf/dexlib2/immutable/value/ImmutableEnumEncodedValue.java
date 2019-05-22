package org.jf.dexlib2.immutable.value;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseEnumEncodedValue;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.value.EnumEncodedValue;

public class ImmutableEnumEncodedValue extends BaseEnumEncodedValue implements ImmutableEncodedValue {
   @Nonnull
   protected final FieldReference value;

   public ImmutableEnumEncodedValue(@Nonnull FieldReference value) {
      this.value = value;
   }

   public static ImmutableEnumEncodedValue of(EnumEncodedValue enumEncodedValue) {
      return enumEncodedValue instanceof ImmutableEnumEncodedValue ? (ImmutableEnumEncodedValue)enumEncodedValue : new ImmutableEnumEncodedValue(enumEncodedValue.getValue());
   }

   @Nonnull
   public FieldReference getValue() {
      return this.value;
   }
}

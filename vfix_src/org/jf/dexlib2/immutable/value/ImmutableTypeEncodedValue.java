package org.jf.dexlib2.immutable.value;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseTypeEncodedValue;
import org.jf.dexlib2.iface.value.TypeEncodedValue;

public class ImmutableTypeEncodedValue extends BaseTypeEncodedValue implements ImmutableEncodedValue {
   @Nonnull
   protected final String value;

   public ImmutableTypeEncodedValue(@Nonnull String value) {
      this.value = value;
   }

   public static ImmutableTypeEncodedValue of(@Nonnull TypeEncodedValue typeEncodedValue) {
      return typeEncodedValue instanceof ImmutableTypeEncodedValue ? (ImmutableTypeEncodedValue)typeEncodedValue : new ImmutableTypeEncodedValue(typeEncodedValue.getValue());
   }

   @Nonnull
   public String getValue() {
      return this.value;
   }
}

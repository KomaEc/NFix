package org.jf.dexlib2.immutable.value;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseMethodEncodedValue;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.value.MethodEncodedValue;

public class ImmutableMethodEncodedValue extends BaseMethodEncodedValue implements ImmutableEncodedValue {
   @Nonnull
   protected final MethodReference value;

   public ImmutableMethodEncodedValue(@Nonnull MethodReference value) {
      this.value = value;
   }

   public static ImmutableMethodEncodedValue of(@Nonnull MethodEncodedValue methodEncodedValue) {
      return methodEncodedValue instanceof ImmutableMethodEncodedValue ? (ImmutableMethodEncodedValue)methodEncodedValue : new ImmutableMethodEncodedValue(methodEncodedValue.getValue());
   }

   @Nonnull
   public MethodReference getValue() {
      return this.value;
   }
}

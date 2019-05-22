package org.jf.dexlib2.immutable.value;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseArrayEncodedValue;
import org.jf.dexlib2.iface.value.ArrayEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;

public class ImmutableArrayEncodedValue extends BaseArrayEncodedValue implements ImmutableEncodedValue {
   @Nonnull
   protected final ImmutableList<? extends ImmutableEncodedValue> value;

   public ImmutableArrayEncodedValue(@Nonnull Collection<? extends EncodedValue> value) {
      this.value = ImmutableEncodedValueFactory.immutableListOf(value);
   }

   public ImmutableArrayEncodedValue(@Nonnull ImmutableList<ImmutableEncodedValue> value) {
      this.value = value;
   }

   public static ImmutableArrayEncodedValue of(@Nonnull ArrayEncodedValue arrayEncodedValue) {
      return arrayEncodedValue instanceof ImmutableArrayEncodedValue ? (ImmutableArrayEncodedValue)arrayEncodedValue : new ImmutableArrayEncodedValue(arrayEncodedValue.getValue());
   }

   @Nonnull
   public ImmutableList<? extends ImmutableEncodedValue> getValue() {
      return this.value;
   }
}

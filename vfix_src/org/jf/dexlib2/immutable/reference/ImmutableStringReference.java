package org.jf.dexlib2.immutable.reference;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseStringReference;
import org.jf.dexlib2.iface.reference.StringReference;

public class ImmutableStringReference extends BaseStringReference implements ImmutableReference {
   @Nonnull
   protected final String str;

   public ImmutableStringReference(String str) {
      this.str = str;
   }

   @Nonnull
   public static ImmutableStringReference of(@Nonnull StringReference stringReference) {
      return stringReference instanceof ImmutableStringReference ? (ImmutableStringReference)stringReference : new ImmutableStringReference(stringReference.getString());
   }

   @Nonnull
   public String getString() {
      return this.str;
   }
}

package org.jf.dexlib2.immutable.reference;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseFieldReference;
import org.jf.dexlib2.iface.reference.FieldReference;

public class ImmutableFieldReference extends BaseFieldReference implements ImmutableReference {
   @Nonnull
   protected final String definingClass;
   @Nonnull
   protected final String name;
   @Nonnull
   protected final String type;

   public ImmutableFieldReference(@Nonnull String definingClass, @Nonnull String name, @Nonnull String type) {
      this.definingClass = definingClass;
      this.name = name;
      this.type = type;
   }

   @Nonnull
   public static ImmutableFieldReference of(@Nonnull FieldReference fieldReference) {
      return fieldReference instanceof ImmutableFieldReference ? (ImmutableFieldReference)fieldReference : new ImmutableFieldReference(fieldReference.getDefiningClass(), fieldReference.getName(), fieldReference.getType());
   }

   @Nonnull
   public String getDefiningClass() {
      return this.definingClass;
   }

   @Nonnull
   public String getName() {
      return this.name;
   }

   @Nonnull
   public String getType() {
      return this.type;
   }
}

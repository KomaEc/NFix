package org.jf.dexlib2.writer.builder;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseFieldReference;

public class BuilderFieldReference extends BaseFieldReference implements BuilderReference {
   @Nonnull
   final BuilderTypeReference definingClass;
   @Nonnull
   final BuilderStringReference name;
   @Nonnull
   final BuilderTypeReference fieldType;
   int index = -1;

   BuilderFieldReference(@Nonnull BuilderTypeReference definingClass, @Nonnull BuilderStringReference name, @Nonnull BuilderTypeReference fieldType) {
      this.definingClass = definingClass;
      this.name = name;
      this.fieldType = fieldType;
   }

   @Nonnull
   public String getDefiningClass() {
      return this.definingClass.getType();
   }

   @Nonnull
   public String getName() {
      return this.name.getString();
   }

   @Nonnull
   public String getType() {
      return this.fieldType.getType();
   }

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index) {
      this.index = index;
   }
}

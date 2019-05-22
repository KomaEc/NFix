package org.jf.dexlib2.writer.builder;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.BaseAnnotationElement;
import org.jf.dexlib2.iface.value.EncodedValue;

public class BuilderAnnotationElement extends BaseAnnotationElement {
   @Nonnull
   final BuilderStringReference name;
   @Nonnull
   final BuilderEncodedValues.BuilderEncodedValue value;

   public BuilderAnnotationElement(@Nonnull BuilderStringReference name, @Nonnull BuilderEncodedValues.BuilderEncodedValue value) {
      this.name = name;
      this.value = value;
   }

   @Nonnull
   public String getName() {
      return this.name.getString();
   }

   @Nonnull
   public EncodedValue getValue() {
      return this.value;
   }
}

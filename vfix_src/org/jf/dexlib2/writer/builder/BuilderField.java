package org.jf.dexlib2.writer.builder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseFieldReference;
import org.jf.dexlib2.iface.Field;

public class BuilderField extends BaseFieldReference implements Field {
   @Nonnull
   final BuilderFieldReference fieldReference;
   final int accessFlags;
   @Nullable
   final BuilderEncodedValues.BuilderEncodedValue initialValue;
   @Nonnull
   final BuilderAnnotationSet annotations;

   BuilderField(@Nonnull BuilderFieldReference fieldReference, int accessFlags, @Nullable BuilderEncodedValues.BuilderEncodedValue initialValue, @Nonnull BuilderAnnotationSet annotations) {
      this.fieldReference = fieldReference;
      this.accessFlags = accessFlags;
      this.initialValue = initialValue;
      this.annotations = annotations;
   }

   public int getAccessFlags() {
      return this.accessFlags;
   }

   @Nullable
   public BuilderEncodedValues.BuilderEncodedValue getInitialValue() {
      return this.initialValue;
   }

   @Nonnull
   public BuilderAnnotationSet getAnnotations() {
      return this.annotations;
   }

   @Nonnull
   public String getDefiningClass() {
      return this.fieldReference.definingClass.getType();
   }

   @Nonnull
   public String getName() {
      return this.fieldReference.name.getString();
   }

   @Nonnull
   public String getType() {
      return this.fieldReference.fieldType.getType();
   }
}

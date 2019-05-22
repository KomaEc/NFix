package org.jf.dexlib2.writer.builder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseMethodParameter;

public class BuilderMethodParameter extends BaseMethodParameter {
   @Nonnull
   final BuilderTypeReference type;
   @Nullable
   final BuilderStringReference name;
   @Nonnull
   final BuilderAnnotationSet annotations;

   public BuilderMethodParameter(@Nonnull BuilderTypeReference type, @Nullable BuilderStringReference name, @Nonnull BuilderAnnotationSet annotations) {
      this.type = type;
      this.name = name;
      this.annotations = annotations;
   }

   @Nonnull
   public String getType() {
      return this.type.getType();
   }

   @Nullable
   public String getName() {
      return this.name == null ? null : this.name.getString();
   }

   @Nonnull
   public BuilderAnnotationSet getAnnotations() {
      return this.annotations;
   }
}

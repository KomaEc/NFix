package org.jf.dexlib2.writer.builder;

import java.util.Set;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.BaseAnnotation;

class BuilderAnnotation extends BaseAnnotation {
   int visibility;
   @Nonnull
   final BuilderTypeReference type;
   @Nonnull
   final Set<? extends BuilderAnnotationElement> elements;
   int offset = 0;

   public BuilderAnnotation(int visibility, @Nonnull BuilderTypeReference type, @Nonnull Set<? extends BuilderAnnotationElement> elements) {
      this.visibility = visibility;
      this.type = type;
      this.elements = elements;
   }

   public int getVisibility() {
      return this.visibility;
   }

   @Nonnull
   public String getType() {
      return this.type.getType();
   }

   @Nonnull
   public Set<? extends BuilderAnnotationElement> getElements() {
      return this.elements;
   }
}

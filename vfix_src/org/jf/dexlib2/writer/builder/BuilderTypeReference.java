package org.jf.dexlib2.writer.builder;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseTypeReference;

public class BuilderTypeReference extends BaseTypeReference implements BuilderReference {
   @Nonnull
   final BuilderStringReference stringReference;
   int index = -1;

   BuilderTypeReference(@Nonnull BuilderStringReference stringReference) {
      this.stringReference = stringReference;
   }

   @Nonnull
   public String getType() {
      return this.stringReference.getString();
   }

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index) {
      this.index = index;
   }
}

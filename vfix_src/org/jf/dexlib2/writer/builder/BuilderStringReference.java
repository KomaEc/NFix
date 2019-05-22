package org.jf.dexlib2.writer.builder;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseStringReference;

public class BuilderStringReference extends BaseStringReference implements BuilderReference {
   @Nonnull
   final String string;
   int index = -1;

   BuilderStringReference(@Nonnull String string) {
      this.string = string;
   }

   @Nonnull
   public String getString() {
      return this.string;
   }

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index) {
      this.index = index;
   }
}

package org.jf.dexlib2.builder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Label {
   @Nullable
   MethodLocation location;

   Label() {
   }

   Label(MethodLocation location) {
      this.location = location;
   }

   public int getCodeAddress() {
      return this.getLocation().getCodeAddress();
   }

   @Nonnull
   public MethodLocation getLocation() {
      if (this.location == null) {
         throw new IllegalStateException("Cannot get the location of a label that hasn't been placed yet.");
      } else {
         return this.location;
      }
   }

   public boolean isPlaced() {
      return this.location != null;
   }
}

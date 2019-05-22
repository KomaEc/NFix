package org.jf.dexlib2.builder;

import javax.annotation.Nullable;
import org.jf.dexlib2.iface.debug.DebugItem;

public abstract class BuilderDebugItem implements DebugItem {
   @Nullable
   MethodLocation location;

   public int getCodeAddress() {
      if (this.location == null) {
         throw new IllegalStateException("Cannot get the address of a BuilderDebugItem that isn't associated with a method.");
      } else {
         return this.location.getCodeAddress();
      }
   }
}

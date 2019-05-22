package org.jf.dexlib2.builder.debug;

import javax.annotation.Nullable;
import org.jf.dexlib2.builder.BuilderDebugItem;
import org.jf.dexlib2.iface.debug.RestartLocal;

public class BuilderRestartLocal extends BuilderDebugItem implements RestartLocal {
   private final int register;

   public BuilderRestartLocal(int register) {
      this.register = register;
   }

   public int getRegister() {
      return this.register;
   }

   @Nullable
   public String getName() {
      return null;
   }

   @Nullable
   public String getType() {
      return null;
   }

   @Nullable
   public String getSignature() {
      return null;
   }

   public int getDebugItemType() {
      return 6;
   }
}

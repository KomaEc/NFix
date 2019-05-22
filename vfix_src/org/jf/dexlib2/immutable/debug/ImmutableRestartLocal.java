package org.jf.dexlib2.immutable.debug;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.debug.RestartLocal;

public class ImmutableRestartLocal extends ImmutableDebugItem implements RestartLocal {
   protected final int register;
   @Nullable
   protected final String name;
   @Nullable
   protected final String type;
   @Nullable
   protected final String signature;

   public ImmutableRestartLocal(int codeAddress, int register) {
      super(codeAddress);
      this.register = register;
      this.name = null;
      this.type = null;
      this.signature = null;
   }

   public ImmutableRestartLocal(int codeAddress, int register, @Nullable String name, @Nullable String type, @Nullable String signature) {
      super(codeAddress);
      this.register = register;
      this.name = name;
      this.type = type;
      this.signature = signature;
   }

   @Nonnull
   public static ImmutableRestartLocal of(@Nonnull RestartLocal restartLocal) {
      return restartLocal instanceof ImmutableRestartLocal ? (ImmutableRestartLocal)restartLocal : new ImmutableRestartLocal(restartLocal.getCodeAddress(), restartLocal.getRegister(), restartLocal.getType(), restartLocal.getName(), restartLocal.getSignature());
   }

   public int getRegister() {
      return this.register;
   }

   @Nullable
   public String getName() {
      return this.name;
   }

   @Nullable
   public String getType() {
      return this.type;
   }

   @Nullable
   public String getSignature() {
      return this.signature;
   }

   public int getDebugItemType() {
      return 6;
   }
}

package org.jf.dexlib2.immutable.debug;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.debug.EndLocal;

public class ImmutableEndLocal extends ImmutableDebugItem implements EndLocal {
   protected final int register;
   @Nullable
   protected final String name;
   @Nullable
   protected final String type;
   @Nullable
   protected final String signature;

   public ImmutableEndLocal(int codeAddress, int register) {
      super(codeAddress);
      this.register = register;
      this.name = null;
      this.type = null;
      this.signature = null;
   }

   public ImmutableEndLocal(int codeAddress, int register, @Nullable String name, @Nullable String type, @Nullable String signature) {
      super(codeAddress);
      this.register = register;
      this.name = name;
      this.type = type;
      this.signature = signature;
   }

   @Nonnull
   public static ImmutableEndLocal of(@Nonnull EndLocal endLocal) {
      return endLocal instanceof ImmutableEndLocal ? (ImmutableEndLocal)endLocal : new ImmutableEndLocal(endLocal.getCodeAddress(), endLocal.getRegister(), endLocal.getType(), endLocal.getName(), endLocal.getSignature());
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
      return 5;
   }
}

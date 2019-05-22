package org.jf.dexlib2.immutable.debug;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseStringReference;
import org.jf.dexlib2.base.reference.BaseTypeReference;
import org.jf.dexlib2.iface.debug.StartLocal;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;

public class ImmutableStartLocal extends ImmutableDebugItem implements StartLocal {
   protected final int register;
   @Nullable
   protected final String name;
   @Nullable
   protected final String type;
   @Nullable
   protected final String signature;

   public ImmutableStartLocal(int codeAddress, int register, @Nullable String name, @Nullable String type, @Nullable String signature) {
      super(codeAddress);
      this.register = register;
      this.name = name;
      this.type = type;
      this.signature = signature;
   }

   @Nonnull
   public static ImmutableStartLocal of(@Nonnull StartLocal startLocal) {
      return startLocal instanceof ImmutableStartLocal ? (ImmutableStartLocal)startLocal : new ImmutableStartLocal(startLocal.getCodeAddress(), startLocal.getRegister(), startLocal.getName(), startLocal.getType(), startLocal.getSignature());
   }

   public int getRegister() {
      return this.register;
   }

   @Nullable
   public StringReference getNameReference() {
      return this.name == null ? null : new BaseStringReference() {
         @Nonnull
         public String getString() {
            return ImmutableStartLocal.this.name;
         }
      };
   }

   @Nullable
   public TypeReference getTypeReference() {
      return this.type == null ? null : new BaseTypeReference() {
         @Nonnull
         public String getType() {
            return ImmutableStartLocal.this.type;
         }
      };
   }

   @Nullable
   public StringReference getSignatureReference() {
      return this.signature == null ? null : new BaseStringReference() {
         @Nonnull
         public String getString() {
            return ImmutableStartLocal.this.signature;
         }
      };
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
      return 3;
   }
}

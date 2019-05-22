package org.jf.dexlib2.builder.debug;

import javax.annotation.Nullable;
import org.jf.dexlib2.builder.BuilderDebugItem;
import org.jf.dexlib2.iface.debug.StartLocal;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;

public class BuilderStartLocal extends BuilderDebugItem implements StartLocal {
   private final int register;
   @Nullable
   private final StringReference name;
   @Nullable
   private final TypeReference type;
   @Nullable
   private final StringReference signature;

   public BuilderStartLocal(int register, @Nullable StringReference name, @Nullable TypeReference type, @Nullable StringReference signature) {
      this.register = register;
      this.name = name;
      this.type = type;
      this.signature = signature;
   }

   public int getRegister() {
      return this.register;
   }

   @Nullable
   public StringReference getNameReference() {
      return this.name;
   }

   @Nullable
   public TypeReference getTypeReference() {
      return this.type;
   }

   @Nullable
   public StringReference getSignatureReference() {
      return this.signature;
   }

   @Nullable
   public String getName() {
      return this.name == null ? null : this.name.getString();
   }

   @Nullable
   public String getType() {
      return this.type == null ? null : this.type.getType();
   }

   @Nullable
   public String getSignature() {
      return this.signature == null ? null : this.signature.getString();
   }

   public int getDebugItemType() {
      return 3;
   }
}

package org.jf.dexlib2.writer.builder;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseMethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodProtoReference;

public class BuilderMethodProtoReference extends BaseMethodProtoReference implements MethodProtoReference, BuilderReference {
   @Nonnull
   final BuilderStringReference shorty;
   @Nonnull
   final BuilderTypeList parameterTypes;
   @Nonnull
   final BuilderTypeReference returnType;
   int index = -1;

   public BuilderMethodProtoReference(@Nonnull BuilderStringReference shorty, @Nonnull BuilderTypeList parameterTypes, @Nonnull BuilderTypeReference returnType) {
      this.shorty = shorty;
      this.parameterTypes = parameterTypes;
      this.returnType = returnType;
   }

   @Nonnull
   public List<? extends CharSequence> getParameterTypes() {
      return this.parameterTypes;
   }

   @Nonnull
   public String getReturnType() {
      return this.returnType.getType();
   }

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index) {
      this.index = index;
   }
}

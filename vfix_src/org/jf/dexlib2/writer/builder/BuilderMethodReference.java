package org.jf.dexlib2.writer.builder;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseMethodReference;

public class BuilderMethodReference extends BaseMethodReference implements BuilderReference {
   @Nonnull
   final BuilderTypeReference definingClass;
   @Nonnull
   final BuilderStringReference name;
   @Nonnull
   final BuilderMethodProtoReference proto;
   int index = -1;

   BuilderMethodReference(@Nonnull BuilderTypeReference definingClass, @Nonnull BuilderStringReference name, @Nonnull BuilderMethodProtoReference proto) {
      this.definingClass = definingClass;
      this.name = name;
      this.proto = proto;
   }

   @Nonnull
   public String getDefiningClass() {
      return this.definingClass.getType();
   }

   @Nonnull
   public String getName() {
      return this.name.getString();
   }

   @Nonnull
   public BuilderTypeList getParameterTypes() {
      return this.proto.parameterTypes;
   }

   @Nonnull
   public String getReturnType() {
      return this.proto.returnType.getType();
   }

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index) {
      this.index = index;
   }
}

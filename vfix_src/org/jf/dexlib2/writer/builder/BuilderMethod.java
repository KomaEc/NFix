package org.jf.dexlib2.writer.builder;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseMethodReference;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;

public class BuilderMethod extends BaseMethodReference implements Method {
   @Nonnull
   final BuilderMethodReference methodReference;
   @Nonnull
   final List<? extends BuilderMethodParameter> parameters;
   final int accessFlags;
   @Nonnull
   final BuilderAnnotationSet annotations;
   @Nullable
   final MethodImplementation methodImplementation;
   int annotationSetRefListOffset = 0;
   int codeItemOffset = 0;

   BuilderMethod(@Nonnull BuilderMethodReference methodReference, @Nonnull List<? extends BuilderMethodParameter> parameters, int accessFlags, @Nonnull BuilderAnnotationSet annotations, @Nullable MethodImplementation methodImplementation) {
      this.methodReference = methodReference;
      this.parameters = parameters;
      this.accessFlags = accessFlags;
      this.annotations = annotations;
      this.methodImplementation = methodImplementation;
   }

   @Nonnull
   public String getDefiningClass() {
      return this.methodReference.definingClass.getType();
   }

   @Nonnull
   public String getName() {
      return this.methodReference.name.getString();
   }

   @Nonnull
   public BuilderTypeList getParameterTypes() {
      return this.methodReference.proto.parameterTypes;
   }

   @Nonnull
   public String getReturnType() {
      return this.methodReference.proto.returnType.getType();
   }

   @Nonnull
   public List<? extends BuilderMethodParameter> getParameters() {
      return this.parameters;
   }

   public int getAccessFlags() {
      return this.accessFlags;
   }

   @Nonnull
   public BuilderAnnotationSet getAnnotations() {
      return this.annotations;
   }

   @Nullable
   public MethodImplementation getImplementation() {
      return this.methodImplementation;
   }
}

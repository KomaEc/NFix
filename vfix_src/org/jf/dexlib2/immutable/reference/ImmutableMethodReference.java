package org.jf.dexlib2.immutable.reference;

import com.google.common.collect.ImmutableList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseMethodReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.immutable.util.CharSequenceConverter;
import org.jf.util.ImmutableUtils;

public class ImmutableMethodReference extends BaseMethodReference implements ImmutableReference {
   @Nonnull
   protected final String definingClass;
   @Nonnull
   protected final String name;
   @Nonnull
   protected final ImmutableList<String> parameters;
   @Nonnull
   protected final String returnType;

   public ImmutableMethodReference(@Nonnull String definingClass, @Nonnull String name, @Nullable Iterable<? extends CharSequence> parameters, @Nonnull String returnType) {
      this.definingClass = definingClass;
      this.name = name;
      this.parameters = CharSequenceConverter.immutableStringList(parameters);
      this.returnType = returnType;
   }

   public ImmutableMethodReference(@Nonnull String definingClass, @Nonnull String name, @Nullable ImmutableList<String> parameters, @Nonnull String returnType) {
      this.definingClass = definingClass;
      this.name = name;
      this.parameters = ImmutableUtils.nullToEmptyList(parameters);
      this.returnType = returnType;
   }

   @Nonnull
   public static ImmutableMethodReference of(@Nonnull MethodReference methodReference) {
      return methodReference instanceof ImmutableMethodReference ? (ImmutableMethodReference)methodReference : new ImmutableMethodReference(methodReference.getDefiningClass(), methodReference.getName(), methodReference.getParameterTypes(), methodReference.getReturnType());
   }

   @Nonnull
   public String getDefiningClass() {
      return this.definingClass;
   }

   @Nonnull
   public String getName() {
      return this.name;
   }

   @Nonnull
   public ImmutableList<String> getParameterTypes() {
      return this.parameters;
   }

   @Nonnull
   public String getReturnType() {
      return this.returnType;
   }
}

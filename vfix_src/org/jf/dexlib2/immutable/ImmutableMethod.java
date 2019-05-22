package org.jf.dexlib2.immutable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseMethodReference;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.util.ImmutableConverter;
import org.jf.util.ImmutableUtils;

public class ImmutableMethod extends BaseMethodReference implements Method {
   @Nonnull
   protected final String definingClass;
   @Nonnull
   protected final String name;
   @Nonnull
   protected final ImmutableList<? extends ImmutableMethodParameter> parameters;
   @Nonnull
   protected final String returnType;
   protected final int accessFlags;
   @Nonnull
   protected final ImmutableSet<? extends ImmutableAnnotation> annotations;
   @Nullable
   protected final ImmutableMethodImplementation methodImplementation;
   private static final ImmutableConverter<ImmutableMethod, Method> CONVERTER = new ImmutableConverter<ImmutableMethod, Method>() {
      protected boolean isImmutable(@Nonnull Method item) {
         return item instanceof ImmutableMethod;
      }

      @Nonnull
      protected ImmutableMethod makeImmutable(@Nonnull Method item) {
         return ImmutableMethod.of(item);
      }
   };

   public ImmutableMethod(@Nonnull String definingClass, @Nonnull String name, @Nullable Iterable<? extends MethodParameter> parameters, @Nonnull String returnType, int accessFlags, @Nullable Set<? extends Annotation> annotations, @Nullable MethodImplementation methodImplementation) {
      this.definingClass = definingClass;
      this.name = name;
      this.parameters = ImmutableMethodParameter.immutableListOf(parameters);
      this.returnType = returnType;
      this.accessFlags = accessFlags;
      this.annotations = ImmutableAnnotation.immutableSetOf(annotations);
      this.methodImplementation = ImmutableMethodImplementation.of(methodImplementation);
   }

   public ImmutableMethod(@Nonnull String definingClass, @Nonnull String name, @Nullable ImmutableList<? extends ImmutableMethodParameter> parameters, @Nonnull String returnType, int accessFlags, @Nullable ImmutableSet<? extends ImmutableAnnotation> annotations, @Nullable ImmutableMethodImplementation methodImplementation) {
      this.definingClass = definingClass;
      this.name = name;
      this.parameters = ImmutableUtils.nullToEmptyList(parameters);
      this.returnType = returnType;
      this.accessFlags = accessFlags;
      this.annotations = ImmutableUtils.nullToEmptySet(annotations);
      this.methodImplementation = methodImplementation;
   }

   public static ImmutableMethod of(Method method) {
      return method instanceof ImmutableMethod ? (ImmutableMethod)method : new ImmutableMethod(method.getDefiningClass(), method.getName(), method.getParameters(), method.getReturnType(), method.getAccessFlags(), method.getAnnotations(), method.getImplementation());
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
   public ImmutableList<? extends CharSequence> getParameterTypes() {
      return this.parameters;
   }

   @Nonnull
   public ImmutableList<? extends ImmutableMethodParameter> getParameters() {
      return this.parameters;
   }

   @Nonnull
   public String getReturnType() {
      return this.returnType;
   }

   public int getAccessFlags() {
      return this.accessFlags;
   }

   @Nonnull
   public ImmutableSet<? extends ImmutableAnnotation> getAnnotations() {
      return this.annotations;
   }

   @Nullable
   public ImmutableMethodImplementation getImplementation() {
      return this.methodImplementation;
   }

   @Nonnull
   public static ImmutableSortedSet<ImmutableMethod> immutableSetOf(@Nullable Iterable<? extends Method> list) {
      return CONVERTER.toSortedSet(Ordering.natural(), (Iterable)list);
   }
}

package org.jf.dexlib2.immutable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseMethodParameter;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.util.ImmutableConverter;
import org.jf.util.ImmutableUtils;

public class ImmutableMethodParameter extends BaseMethodParameter {
   @Nonnull
   protected final String type;
   @Nonnull
   protected final ImmutableSet<? extends ImmutableAnnotation> annotations;
   @Nullable
   protected final String name;
   private static final ImmutableConverter<ImmutableMethodParameter, MethodParameter> CONVERTER = new ImmutableConverter<ImmutableMethodParameter, MethodParameter>() {
      protected boolean isImmutable(@Nonnull MethodParameter item) {
         return item instanceof ImmutableMethodParameter;
      }

      @Nonnull
      protected ImmutableMethodParameter makeImmutable(@Nonnull MethodParameter item) {
         return ImmutableMethodParameter.of(item);
      }
   };

   public ImmutableMethodParameter(@Nonnull String type, @Nullable Set<? extends Annotation> annotations, @Nullable String name) {
      this.type = type;
      this.annotations = ImmutableAnnotation.immutableSetOf(annotations);
      this.name = name;
   }

   public ImmutableMethodParameter(@Nonnull String type, @Nullable ImmutableSet<? extends ImmutableAnnotation> annotations, @Nullable String name) {
      this.type = type;
      this.annotations = ImmutableUtils.nullToEmptySet(annotations);
      this.name = name;
   }

   public static ImmutableMethodParameter of(MethodParameter methodParameter) {
      return methodParameter instanceof ImmutableMethodParameter ? (ImmutableMethodParameter)methodParameter : new ImmutableMethodParameter(methodParameter.getType(), methodParameter.getAnnotations(), methodParameter.getName());
   }

   @Nonnull
   public String getType() {
      return this.type;
   }

   @Nonnull
   public Set<? extends Annotation> getAnnotations() {
      return this.annotations;
   }

   @Nullable
   public String getName() {
      return this.name;
   }

   @Nullable
   public String getSignature() {
      return null;
   }

   @Nonnull
   public static ImmutableList<ImmutableMethodParameter> immutableListOf(@Nullable Iterable<? extends MethodParameter> list) {
      return CONVERTER.toList(list);
   }
}

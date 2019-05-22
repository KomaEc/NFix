package org.jf.dexlib2.analysis.reflection;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.analysis.reflection.util.ReflectionUtils;
import org.jf.dexlib2.base.reference.BaseFieldReference;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.value.EncodedValue;

public class ReflectionField extends BaseFieldReference implements Field {
   private final java.lang.reflect.Field field;

   public ReflectionField(java.lang.reflect.Field field) {
      this.field = field;
   }

   public int getAccessFlags() {
      return this.field.getModifiers();
   }

   @Nullable
   public EncodedValue getInitialValue() {
      return null;
   }

   @Nonnull
   public Set<? extends Annotation> getAnnotations() {
      return ImmutableSet.of();
   }

   @Nonnull
   public String getDefiningClass() {
      return ReflectionUtils.javaToDexName(this.field.getDeclaringClass().getName());
   }

   @Nonnull
   public String getName() {
      return this.field.getName();
   }

   @Nonnull
   public String getType() {
      return ReflectionUtils.javaToDexName(this.field.getType().getName());
   }
}

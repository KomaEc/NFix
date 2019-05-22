package org.jf.dexlib2.analysis.reflection;

import com.google.common.collect.ImmutableSet;
import java.util.AbstractList;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.analysis.reflection.util.ReflectionUtils;
import org.jf.dexlib2.base.BaseMethodParameter;
import org.jf.dexlib2.base.reference.BaseMethodReference;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.MethodParameter;

public class ReflectionMethod extends BaseMethodReference implements Method {
   private final java.lang.reflect.Method method;

   public ReflectionMethod(java.lang.reflect.Method method) {
      this.method = method;
   }

   @Nonnull
   public List<? extends MethodParameter> getParameters() {
      final java.lang.reflect.Method method = this.method;
      return new AbstractList<MethodParameter>() {
         private final Class[] parameters = method.getParameterTypes();

         public MethodParameter get(final int index) {
            return new BaseMethodParameter() {
               @Nonnull
               public Set<? extends Annotation> getAnnotations() {
                  return ImmutableSet.of();
               }

               @Nullable
               public String getName() {
                  return null;
               }

               @Nonnull
               public String getType() {
                  return ReflectionUtils.javaToDexName(parameters[index].getName());
               }
            };
         }

         public int size() {
            return this.parameters.length;
         }
      };
   }

   public int getAccessFlags() {
      return this.method.getModifiers();
   }

   @Nonnull
   public Set<? extends Annotation> getAnnotations() {
      return ImmutableSet.of();
   }

   @Nullable
   public MethodImplementation getImplementation() {
      return null;
   }

   @Nonnull
   public String getDefiningClass() {
      return ReflectionUtils.javaToDexName(this.method.getDeclaringClass().getName());
   }

   @Nonnull
   public String getName() {
      return this.method.getName();
   }

   @Nonnull
   public List<String> getParameterTypes() {
      return new AbstractList<String>() {
         private final List<? extends MethodParameter> parameters = ReflectionMethod.this.getParameters();

         public String get(int index) {
            return ((MethodParameter)this.parameters.get(index)).getType();
         }

         public int size() {
            return this.parameters.size();
         }
      };
   }

   @Nonnull
   public String getReturnType() {
      return ReflectionUtils.javaToDexName(this.method.getReturnType().getName());
   }
}

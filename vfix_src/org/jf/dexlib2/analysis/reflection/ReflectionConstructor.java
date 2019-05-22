package org.jf.dexlib2.analysis.reflection;

import com.google.common.collect.ImmutableSet;
import java.lang.reflect.Constructor;
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

public class ReflectionConstructor extends BaseMethodReference implements Method {
   private final Constructor constructor;

   public ReflectionConstructor(Constructor constructor) {
      this.constructor = constructor;
   }

   @Nonnull
   public List<? extends MethodParameter> getParameters() {
      final Constructor method = this.constructor;
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
      return this.constructor.getModifiers();
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
      return ReflectionUtils.javaToDexName(this.constructor.getDeclaringClass().getName());
   }

   @Nonnull
   public String getName() {
      return this.constructor.getName();
   }

   @Nonnull
   public List<String> getParameterTypes() {
      return new AbstractList<String>() {
         private final List<? extends MethodParameter> parameters = ReflectionConstructor.this.getParameters();

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
      return "V";
   }
}

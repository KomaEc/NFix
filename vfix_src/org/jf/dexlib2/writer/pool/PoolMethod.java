package org.jf.dexlib2.writer.pool;

import com.google.common.base.Function;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseMethodReference;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.MethodParameter;

class PoolMethod extends BaseMethodReference implements Method {
   @Nonnull
   private final Method method;
   protected int annotationSetRefListOffset = 0;
   protected int codeItemOffset = 0;
   public static final Function<Method, PoolMethod> TRANSFORM = new Function<Method, PoolMethod>() {
      public PoolMethod apply(Method method) {
         return new PoolMethod(method);
      }
   };

   PoolMethod(@Nonnull Method method) {
      this.method = method;
   }

   @Nonnull
   public String getDefiningClass() {
      return this.method.getDefiningClass();
   }

   @Nonnull
   public String getName() {
      return this.method.getName();
   }

   @Nonnull
   public List<? extends CharSequence> getParameterTypes() {
      return this.method.getParameterTypes();
   }

   @Nonnull
   public List<? extends MethodParameter> getParameters() {
      return this.method.getParameters();
   }

   @Nonnull
   public String getReturnType() {
      return this.method.getReturnType();
   }

   public int getAccessFlags() {
      return this.method.getAccessFlags();
   }

   @Nonnull
   public Set<? extends Annotation> getAnnotations() {
      return this.method.getAnnotations();
   }

   @Nullable
   public MethodImplementation getImplementation() {
      return this.method.getImplementation();
   }
}

package org.codehaus.groovy.runtime.metaclass;

import groovy.lang.MetaMethod;
import java.lang.reflect.InvocationTargetException;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.CachedMethod;
import org.codehaus.groovy.runtime.InvokerInvocationException;

public class ReflectionMetaMethod extends MetaMethod {
   protected final CachedMethod method;

   public ReflectionMetaMethod(CachedMethod method) {
      this.method = method;
      this.setParametersTypes(method.getParameterTypes());
   }

   public int getModifiers() {
      return this.method.getModifiers();
   }

   public String getName() {
      return this.method.getName();
   }

   public Class getReturnType() {
      return this.method.getReturnType();
   }

   public CachedClass getDeclaringClass() {
      return this.method.cachedClass;
   }

   public Object invoke(Object object, Object[] arguments) {
      try {
         return this.method.setAccessible().invoke(object, arguments);
      } catch (IllegalArgumentException var4) {
         throw new InvokerInvocationException(var4);
      } catch (IllegalAccessException var5) {
         throw new InvokerInvocationException(var5);
      } catch (InvocationTargetException var6) {
         throw var6.getCause() instanceof RuntimeException ? (RuntimeException)var6.getCause() : new InvokerInvocationException(var6);
      }
   }

   public String toString() {
      return this.method.toString();
   }

   protected Class[] getPT() {
      return this.method.getNativeParameterTypes();
   }
}

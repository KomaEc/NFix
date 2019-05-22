package org.codehaus.groovy.reflection;

import groovy.lang.GroovyRuntimeException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.InvokerInvocationException;

public class CachedConstructor extends ParameterTypes {
   CachedClass clazz;
   public final Constructor cachedConstructor;

   public CachedConstructor(CachedClass clazz, final Constructor c) {
      this.cachedConstructor = c;
      this.clazz = clazz;

      try {
         AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
               c.setAccessible(true);
               return null;
            }
         });
      } catch (SecurityException var4) {
      }

   }

   public CachedConstructor(Constructor c) {
      this(ReflectionCache.getCachedClass(c.getDeclaringClass()), c);
   }

   protected Class[] getPT() {
      return this.cachedConstructor.getParameterTypes();
   }

   public static CachedConstructor find(Constructor constructor) {
      CachedConstructor[] constructors = ReflectionCache.getCachedClass(constructor.getDeclaringClass()).getConstructors();

      for(int i = 0; i < constructors.length; ++i) {
         CachedConstructor cachedConstructor = constructors[i];
         if (cachedConstructor.cachedConstructor.equals(constructor)) {
            return cachedConstructor;
         }
      }

      throw new RuntimeException("Couldn't find method: " + constructor);
   }

   public Object doConstructorInvoke(Object[] argumentArray) {
      argumentArray = this.coerceArgumentsToClasses(argumentArray);
      return this.invoke(argumentArray);
   }

   public Object invoke(Object[] argumentArray) {
      Constructor constr = this.cachedConstructor;

      try {
         return constr.newInstance(argumentArray);
      } catch (InvocationTargetException var4) {
         throw var4.getCause() instanceof RuntimeException ? (RuntimeException)var4.getCause() : new InvokerInvocationException(var4);
      } catch (IllegalArgumentException var5) {
         throw createExceptionText("failed to invoke constructor: ", constr, argumentArray, var5, false);
      } catch (IllegalAccessException var6) {
         throw createExceptionText("could not access constructor: ", constr, argumentArray, var6, false);
      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw createExceptionText("failed to invoke constructor: ", constr, argumentArray, var7, true);
         }
      }
   }

   private static GroovyRuntimeException createExceptionText(String init, Constructor constructor, Object[] argumentArray, Throwable e, boolean setReason) {
      throw new GroovyRuntimeException(init + constructor + " with arguments: " + InvokerHelper.toString(argumentArray) + " reason: " + e, setReason ? e : null);
   }

   public int getModifiers() {
      return this.cachedConstructor.getModifiers();
   }
}

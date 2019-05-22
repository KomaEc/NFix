package org.codehaus.groovy.runtime;

import groovy.lang.GroovyRuntimeException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class ConversionHandler implements InvocationHandler, Serializable {
   private Object delegate;
   private static final long serialVersionUID = 1162833717190835227L;

   public ConversionHandler(Object delegate) {
      if (delegate == null) {
         throw new IllegalArgumentException("delegate must not be null");
      } else {
         this.delegate = delegate;
      }
   }

   public Object getDelegate() {
      return this.delegate;
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if (!this.checkMethod(method)) {
         try {
            return this.invokeCustom(proxy, method, args);
         } catch (GroovyRuntimeException var5) {
            throw ScriptBytecodeAdapter.unwrap(var5);
         }
      } else {
         try {
            return method.invoke(this, args);
         } catch (InvocationTargetException var6) {
            throw var6.getTargetException();
         }
      }
   }

   protected boolean checkMethod(Method method) {
      return isCoreObjectMethod(method);
   }

   public abstract Object invokeCustom(Object var1, Method var2, Object[] var3) throws Throwable;

   public boolean equals(Object obj) {
      if (obj instanceof Proxy) {
         obj = Proxy.getInvocationHandler(obj);
      }

      return obj instanceof ConversionHandler ? ((ConversionHandler)obj).getDelegate().equals(this.delegate) : false;
   }

   public int hashCode() {
      return this.delegate.hashCode();
   }

   public String toString() {
      return this.delegate.toString();
   }

   public static boolean isCoreObjectMethod(Method method) {
      return Object.class.equals(method.getDeclaringClass());
   }
}

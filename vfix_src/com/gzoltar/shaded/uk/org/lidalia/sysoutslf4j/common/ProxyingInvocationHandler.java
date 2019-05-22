package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ProxyingInvocationHandler implements InvocationHandler {
   private final Object target;
   private final Map<Method, Method> methods = new HashMap();

   ProxyingInvocationHandler(Object target, Class<?> interfaceClass) {
      this.target = target;
      Method[] var3;
      int var4 = (var3 = interfaceClass.getMethods()).length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Method method = var3[var5];

         try {
            Method methodOnTarget = target.getClass().getMethod(method.getName(), method.getParameterTypes());
            this.methods.put(method, methodOnTarget);
         } catch (NoSuchMethodException var8) {
            throw new IllegalArgumentException("Target " + target + " does not have methods to match all method signatures on class " + interfaceClass, var8);
         }
      }

   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      Method methodOnTarget = (Method)this.methods.get(method);
      return methodOnTarget.invoke(this.target, args);
   }

   public Object getTarget() {
      return this.target;
   }
}

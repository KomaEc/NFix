package com.google.common.reflect;

import com.google.common.collect.ImmutableMap;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessControlException;

final class Types$TypeVariableInvocationHandler implements InvocationHandler {
   private static final ImmutableMap<String, Method> typeVariableMethods;
   private final Types.TypeVariableImpl<?> typeVariableImpl;

   Types$TypeVariableInvocationHandler(Types.TypeVariableImpl<?> typeVariableImpl) {
      this.typeVariableImpl = typeVariableImpl;
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      String methodName = method.getName();
      Method typeVariableMethod = (Method)typeVariableMethods.get(methodName);
      if (typeVariableMethod == null) {
         throw new UnsupportedOperationException(methodName);
      } else {
         try {
            return typeVariableMethod.invoke(this.typeVariableImpl, args);
         } catch (InvocationTargetException var7) {
            throw var7.getCause();
         }
      }
   }

   // $FF: synthetic method
   static Types.TypeVariableImpl access$600(Types$TypeVariableInvocationHandler x0) {
      return x0.typeVariableImpl;
   }

   static {
      ImmutableMap.Builder<String, Method> builder = ImmutableMap.builder();
      Method[] var1 = Types.TypeVariableImpl.class.getMethods();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Method method = var1[var3];
         if (method.getDeclaringClass().equals(Types.TypeVariableImpl.class)) {
            try {
               method.setAccessible(true);
            } catch (AccessControlException var6) {
            }

            builder.put(method.getName(), method);
         }
      }

      typeVariableMethods = builder.build();
   }
}

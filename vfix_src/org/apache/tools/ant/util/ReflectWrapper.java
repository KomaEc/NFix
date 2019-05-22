package org.apache.tools.ant.util;

import java.lang.reflect.Constructor;

public class ReflectWrapper {
   private Object obj;

   public ReflectWrapper(ClassLoader loader, String name) {
      try {
         Class clazz = Class.forName(name, true, loader);
         Constructor constructor = clazz.getConstructor((Class[])null);
         this.obj = constructor.newInstance((Object[])null);
      } catch (Exception var5) {
         ReflectUtil.throwBuildException(var5);
      }

   }

   public ReflectWrapper(Object obj) {
      this.obj = obj;
   }

   public Object getObject() {
      return this.obj;
   }

   public Object invoke(String methodName) {
      return ReflectUtil.invoke(this.obj, methodName);
   }

   public Object invoke(String methodName, Class argType, Object arg) {
      return ReflectUtil.invoke(this.obj, methodName, argType, arg);
   }

   public Object invoke(String methodName, Class argType1, Object arg1, Class argType2, Object arg2) {
      return ReflectUtil.invoke(this.obj, methodName, argType1, arg1, argType2, arg2);
   }
}

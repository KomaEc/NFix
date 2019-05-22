package soot.rtlib.tamiflex;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface IUnexpectedReflectiveCallHandler {
   void classNewInstance(Class<?> var1);

   void classForName(String var1);

   void constructorNewInstance(Constructor<?> var1);

   void methodInvoke(Object var1, Method var2);

   void fieldSet(Object var1, Field var2);

   void fieldGet(Object var1, Field var2);
}

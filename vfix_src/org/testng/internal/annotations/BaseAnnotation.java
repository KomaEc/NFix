package org.testng.internal.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class BaseAnnotation {
   private Class m_testClass;
   private Method m_method;
   private Constructor m_constructor;

   public Constructor getConstructor() {
      return this.m_constructor;
   }

   public void setConstructor(Constructor constructor) {
      this.m_constructor = constructor;
   }

   public Method getMethod() {
      return this.m_method;
   }

   public void setMethod(Method method) {
      this.m_method = method;
   }

   public Class getTestClass() {
      return this.m_testClass;
   }

   public void setTestClass(Class testClass) {
      this.m_testClass = testClass;
   }
}

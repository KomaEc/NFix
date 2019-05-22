package org.testng.junit;

import java.lang.reflect.Method;
import org.testng.ITestNGMethod;
import org.testng.internal.BaseTestMethod;
import org.testng.internal.annotations.IAnnotationFinder;

public abstract class JUnitTestMethod extends BaseTestMethod {
   protected JUnitTestMethod(JUnitTestClass owner, Method method, Object instance) {
      this(owner, method.getName(), method, instance);
   }

   protected JUnitTestMethod(JUnitTestClass owner, String methodName, Method method, Object instance) {
      super(methodName, (Method)method, (IAnnotationFinder)null, instance);
      this.setTestClass(owner);
      owner.getTestMethodList().add(this);
   }

   public boolean isTest() {
      return true;
   }

   public ITestNGMethod clone() {
      throw new IllegalStateException("clone is not supported for JUnit");
   }
}

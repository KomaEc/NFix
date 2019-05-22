package org.testng.junit;

import java.lang.reflect.Method;
import org.junit.runner.Description;
import org.testng.internal.Utils;

public class JUnit4TestMethod extends JUnitTestMethod {
   public JUnit4TestMethod(JUnitTestClass owner, Description desc) {
      super(owner, desc.getMethodName(), getMethod(desc), desc);
   }

   public Object[] getInstances() {
      return new Object[0];
   }

   private static Method getMethod(Description desc) {
      Class<?> c = desc.getTestClass();
      String method = desc.getMethodName();
      int idx = method.indexOf(91);
      if (idx != -1) {
         method = method.substring(0, idx);
      }

      try {
         return c.getMethod(method);
      } catch (Throwable var5) {
         Utils.log("JUnit4TestMethod", 2, "Method '" + method + "' not found in class '" + c.getName() + "': " + var5.getMessage());
         return null;
      }
   }
}

package org.testng;

import java.lang.reflect.Method;
import org.testng.internal.ClonedMethod;

public class TestNGUtils {
   public static ITestNGMethod createITestNGMethod(ITestNGMethod existingMethod, Method method) {
      return new ClonedMethod(existingMethod, method);
   }
}

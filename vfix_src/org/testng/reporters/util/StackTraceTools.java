package org.testng.reporters.util;

import org.testng.ITestNGMethod;

public class StackTraceTools {
   public static int getTestRoot(StackTraceElement[] stack, ITestNGMethod method) {
      if (stack != null) {
         String cname = method.getTestClass().getName();

         for(int x = stack.length - 1; x >= 0; --x) {
            if (cname.equals(stack[x].getClassName()) && method.getMethodName().equals(stack[x].getMethodName())) {
               return x;
            }
         }

         return stack.length - 1;
      } else {
         return -1;
      }
   }

   public static StackTraceElement[] getTestNGInstrastructure(StackTraceElement[] stack, ITestNGMethod method) {
      int slot = getTestRoot(stack, method);
      if (slot < 0) {
         return new StackTraceElement[0];
      } else {
         StackTraceElement[] r = new StackTraceElement[stack.length - slot];

         for(int x = 0; x < r.length; ++x) {
            r[x] = stack[x + slot];
         }

         return r;
      }
   }
}

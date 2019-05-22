package com.gzoltar.instrumentation.testing.junit;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

public class JUnitFilter extends Filter {
   private final String methodName;

   public JUnitFilter(String var1) {
      this.methodName = var1;
   }

   public boolean shouldRun(Description var1) {
      return var1.getMethodName().equals(this.methodName) && var1.isTest();
   }

   public String describe() {
      return null;
   }
}

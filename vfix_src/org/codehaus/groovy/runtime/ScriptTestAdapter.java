package org.codehaus.groovy.runtime;

import junit.framework.Test;
import junit.framework.TestResult;

public class ScriptTestAdapter implements Test {
   private Class scriptClass;
   private String[] arguments;

   public ScriptTestAdapter(Class scriptClass, String[] arguments) {
      this.scriptClass = scriptClass;
      this.arguments = arguments;
   }

   public int countTestCases() {
      return 1;
   }

   public void run(TestResult result) {
      try {
         result.startTest(this);
         InvokerHelper.runScript(this.scriptClass, this.arguments);
         result.endTest(this);
      } catch (Exception var3) {
         result.addError(this, var3);
      }

   }

   public String toString() {
      return "TestCase for script: " + this.scriptClass.getName();
   }
}

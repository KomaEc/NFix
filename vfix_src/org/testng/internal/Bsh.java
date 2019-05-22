package org.testng.internal;

import bsh.EvalError;
import bsh.Interpreter;
import java.lang.reflect.Method;
import java.util.Map;
import org.testng.ITestNGMethod;
import org.testng.TestNGException;
import org.testng.collections.Maps;

public class Bsh implements IBsh {
   private static Interpreter s_interpreter;

   public boolean includeMethodFromExpression(String expression, ITestNGMethod tm) {
      boolean result = false;
      Interpreter interpreter = getInterpreter();

      try {
         Map<String, String> groups = Maps.newHashMap();
         String[] arr$ = tm.getGroups();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String group = arr$[i$];
            groups.put(group, group);
         }

         this.setContext(interpreter, tm.getMethod(), groups, tm);
         Object evalResult = interpreter.eval(expression);
         result = (Boolean)evalResult;
      } catch (EvalError var13) {
         Utils.log("bsh.Interpreter", 2, "Cannot evaluate expression:" + expression + ":" + var13.getMessage());
      } finally {
         this.resetContext(interpreter);
      }

      return result;
   }

   private static Interpreter getInterpreter() {
      if (null == s_interpreter) {
         s_interpreter = new Interpreter();
      }

      return s_interpreter;
   }

   private void setContext(Interpreter interpreter, Method method, Map<String, String> groups, ITestNGMethod tm) {
      try {
         interpreter.set("method", method);
         interpreter.set("groups", groups);
         interpreter.set("testngMethod", tm);
      } catch (EvalError var6) {
         throw new TestNGException("Cannot set BSH interpreter", var6);
      }
   }

   private void resetContext(Interpreter interpreter) {
      try {
         interpreter.unset("method");
         interpreter.unset("groups");
         interpreter.unset("testngMethod");
      } catch (EvalError var3) {
         Utils.log("bsh.Interpreter", 2, "Cannot reset interpreter:" + var3.getMessage());
      }

   }
}

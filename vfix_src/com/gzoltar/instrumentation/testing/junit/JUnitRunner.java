package com.gzoltar.instrumentation.testing.junit;

import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.testing.TestRunner;
import com.gzoltar.instrumentation.testing.jobs.JobDefinition;
import com.gzoltar.instrumentation.testing.jobs.JobHandler;
import com.gzoltar.instrumentation.testing.launch.ExecutionParameters;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Request;

public class JUnitRunner extends TestRunner {
   public JUnitRunner(ExecutionParameters var1) {
      super(var1);
   }

   public List<JobHandler> createJobs(String var1) {
      ArrayList var2 = new ArrayList();

      Class var3;
      try {
         var3 = Class.forName(var1, false, JUnitRunner.class.getClassLoader());
      } catch (ClassNotFoundException var12) {
         Logger.getInstance().err("Class " + var1 + " not found.", var12);
         return null;
      }

      assert var3 != null;

      HashMap var4 = new HashMap();
      Iterator var5 = Request.aClass(var3).getRunner().getDescription().getChildren().iterator();

      while(true) {
         while(var5.hasNext()) {
            Description var6;
            Object var10000;
            if ((var6 = (Description)var5.next()).getMethodName() == null) {
               Method[] var14;
               int var8 = (var14 = var3.getMethods()).length;

               for(int var9 = 0; var9 < var8; ++var9) {
                  Method var10;
                  if ((var10 = var14[var9]).isAnnotationPresent(Test.class) || Modifier.isPublic(var10.getModifiers()) && var10.getReturnType().equals(Void.TYPE) && var10.getParameterTypes().length == 0 && var10.getName().startsWith("test")) {
                     var10000 = var4.containsKey(var1) ? (Set)var4.get(var1) : new HashSet();
                     Object var11 = var10000;
                     ((Set)var10000).add(var10.getName() + var6.getDisplayName());
                     var4.put(var1, var11);
                  }
               }
            } else {
               var10000 = var4.containsKey(var6.getClassName()) ? (Set)var4.get(var6.getClassName()) : new HashSet();
               Object var7 = var10000;
               ((Set)var10000).add(var6.getMethodName());
               var4.put(var6.getClassName(), var7);
            }
         }

         if (var4.isEmpty()) {
            return var2;
         }

         var5 = var4.keySet().iterator();

         while(var5.hasNext()) {
            String var13 = (String)var5.next();
            Iterator var15 = ((Set)var4.get(var13)).iterator();

            while(var15.hasNext()) {
               String var16 = (String)var15.next();
               JobDefinition var17 = new JobDefinition(RunJUniTestMethod.class.getCanonicalName(), var13, var16, this.executionData);
               JobHandler var18 = new JobHandler(var17);
               var2.add(var18);
            }
         }

         return var2;
      }
   }
}

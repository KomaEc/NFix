package com.gzoltar.instrumentation.testing.testng;

import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.testing.TestRunner;
import com.gzoltar.instrumentation.testing.jobs.JobHandler;
import com.gzoltar.instrumentation.testing.launch.ExecutionParameters;
import java.util.List;
import org.testng.ITestListener;
import org.testng.TestNG;

public class TestNGRunner extends TestRunner {
   public TestNGRunner(ExecutionParameters var1) {
      super(var1);
   }

   public List<JobHandler> createJobs(String var1) {
      Class var2 = null;

      try {
         var2 = Class.forName(var1, false, TestNGRunner.class.getClassLoader());
      } catch (ClassNotFoundException var4) {
         Logger.getInstance().err("Class " + var1 + " not found.", var4);
      }

      assert var2 != null;

      TestNGListener var3 = new TestNGListener();
      TestNG var5;
      (var5 = new TestNG()).setTestClasses(new Class[]{var2});
      var5.addListener((ITestListener)var3);
      var5.setThreadCount(1);
      var5.setParallel("classes");
      var5.setPreserveOrder(true);
      var5.setVerbose(0);
      var5.setUseDefaultListeners(false);
      var5.run();
      return null;
   }
}

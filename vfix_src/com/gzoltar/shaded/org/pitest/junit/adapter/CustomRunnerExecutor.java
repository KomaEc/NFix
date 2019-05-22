package com.gzoltar.shaded.org.pitest.junit.adapter;

import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.testapi.ResultCollector;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

public class CustomRunnerExecutor {
   private final Description description;
   private final Runner runner;
   private final ResultCollector rc;

   public CustomRunnerExecutor(Description description, Runner runner, ResultCollector rc) {
      this.runner = runner;
      this.rc = rc;
      this.description = description;
   }

   public void run() {
      RunNotifier rn = new RunNotifier();
      RunListener listener = new AdaptingRunListener(this.description, this.rc);
      rn.addFirstListener(listener);
      this.runner.run(rn);
   }
}

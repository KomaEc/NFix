package com.gzoltar.shaded.org.pitest.junit.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

public class ForeignClassLoaderCustomRunnerExecutor implements Callable<List<String>> {
   private final Runner runner;

   public ForeignClassLoaderCustomRunnerExecutor(Runner runner) {
      this.runner = runner;
   }

   public List<String> call() {
      List<String> queue = new ArrayList();
      RunNotifier rn = new RunNotifier();
      RunListener listener = new ForeignClassLoaderAdaptingRunListener(queue);
      rn.addFirstListener(listener);
      this.runner.run(rn);
      return queue;
   }
}

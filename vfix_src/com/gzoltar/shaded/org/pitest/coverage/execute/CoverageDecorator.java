package com.gzoltar.shaded.org.pitest.coverage.execute;

import com.gzoltar.shaded.org.pitest.coverage.CoverageReceiver;
import com.gzoltar.shaded.org.pitest.extension.common.TestUnitDecorator;
import com.gzoltar.shaded.org.pitest.testapi.ResultCollector;
import com.gzoltar.shaded.org.pitest.testapi.TestUnit;
import com.gzoltar.shaded.org.pitest.testapi.execute.ExitingResultCollector;
import com.gzoltar.shaded.org.pitest.util.Log;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.logging.Logger;

public class CoverageDecorator extends TestUnitDecorator {
   private static final Logger LOG = Log.getLogger();
   private final CoverageReceiver invokeQueue;
   private final ThreadMXBean threads = ManagementFactory.getThreadMXBean();

   protected CoverageDecorator(CoverageReceiver queue, TestUnit child) {
      super(child);
      this.invokeQueue = queue;
   }

   public void execute(ClassLoader loader, ResultCollector rc) {
      LOG.fine("Gathering coverage for test " + this.child().getDescription());
      this.invokeQueue.newTest();
      int threadsBeforeTest = this.threads.getThreadCount();
      long t0 = System.currentTimeMillis();
      ExitingResultCollector wrappedCollector = new ExitingResultCollector(rc);
      this.child().execute(loader, wrappedCollector);
      int executionTime = (int)(System.currentTimeMillis() - t0);
      int threadsAfterTest = this.threads.getThreadCount();
      if (threadsAfterTest > threadsBeforeTest) {
         LOG.warning("More threads at end of test (" + threadsAfterTest + ") " + this.child().getDescription().getName() + " than start. (" + threadsBeforeTest + ")");
      }

      this.invokeQueue.recordTestOutcome(this.child().getDescription(), !wrappedCollector.shouldExit(), executionTime);
   }
}

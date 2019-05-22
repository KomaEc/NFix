package com.gzoltar.shaded.org.pitest.testapi.execute;

import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import com.gzoltar.shaded.org.pitest.testapi.Configuration;
import com.gzoltar.shaded.org.pitest.testapi.TestListener;
import com.gzoltar.shaded.org.pitest.testapi.TestResult;
import com.gzoltar.shaded.org.pitest.testapi.TestUnit;
import com.gzoltar.shaded.org.pitest.util.Log;
import com.gzoltar.shaded.org.pitest.util.PitError;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class Pitest {
   private static final Logger LOG = Log.getLogger();
   private final List<? extends TestListener> listeners;

   public Pitest(List<? extends TestListener> listeners) {
      this.listeners = listeners;
   }

   public void run(Container container, List<? extends TestUnit> testUnits) {
      LOG.fine("Running " + testUnits.size() + " units");
      this.signalRunStartToAllListeners();
      this.executeTests(container, testUnits);
      this.signalRunEndToAllListeners();
   }

   private void executeTests(Container container, List<? extends TestUnit> testUnits) {
      Iterator i$ = testUnits.iterator();

      while(i$.hasNext()) {
         TestUnit unit = (TestUnit)i$.next();
         List<TestResult> results = container.execute(unit);
         this.processResults(results);
      }

   }

   /** @deprecated */
   @Deprecated
   public void run(Container defaultContainer, Configuration config, Class<?>... classes) {
      this.run(defaultContainer, config, (Collection)Arrays.asList(classes));
   }

   private void run(Container container, Configuration config, Collection<Class<?>> classes) {
      FindTestUnits find = new FindTestUnits(config);
      this.run(container, find.findTestUnitsForAllSuppliedClasses(classes));
   }

   private void processResults(List<TestResult> results) {
      Iterator i$ = results.iterator();

      while(i$.hasNext()) {
         TestResult result = (TestResult)i$.next();
         ResultType classifiedResult = this.classify(result);
         FCollection.forEach(this.listeners, classifiedResult.getListenerFunction(result));
      }

   }

   private void signalRunStartToAllListeners() {
      FCollection.forEach(this.listeners, new SideEffect1<TestListener>() {
         public void apply(TestListener a) {
            a.onRunStart();
         }
      });
   }

   private void signalRunEndToAllListeners() {
      FCollection.forEach(this.listeners, new SideEffect1<TestListener>() {
         public void apply(TestListener a) {
            a.onRunEnd();
         }
      });
   }

   private ResultType classify(TestResult result) {
      switch(result.getState()) {
      case STARTED:
         return ResultType.STARTED;
      case NOT_RUN:
         return ResultType.SKIPPED;
      case FINISHED:
         return this.classifyFinishedTest(result);
      default:
         throw new PitError("Unhandled state");
      }
   }

   private ResultType classifyFinishedTest(TestResult result) {
      return result.getThrowable() != null ? ResultType.FAIL : ResultType.PASS;
   }
}

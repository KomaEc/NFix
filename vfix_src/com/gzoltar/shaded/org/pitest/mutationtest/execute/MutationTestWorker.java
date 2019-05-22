package com.gzoltar.shaded.org.pitest.mutationtest.execute;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.classpath.ClassPath;
import com.gzoltar.shaded.org.pitest.functional.F3;
import com.gzoltar.shaded.org.pitest.mutationtest.DetectionStatus;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationStatusTestPair;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.Mutant;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.Mutater;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.mutationtest.mocksupport.JavassistInterceptor;
import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.testapi.TestResult;
import com.gzoltar.shaded.org.pitest.testapi.TestUnit;
import com.gzoltar.shaded.org.pitest.testapi.execute.Container;
import com.gzoltar.shaded.org.pitest.testapi.execute.ExitingResultCollector;
import com.gzoltar.shaded.org.pitest.testapi.execute.MultipleTestGroup;
import com.gzoltar.shaded.org.pitest.testapi.execute.Pitest;
import com.gzoltar.shaded.org.pitest.testapi.execute.containers.ConcreteResultCollector;
import com.gzoltar.shaded.org.pitest.testapi.execute.containers.UnContainer;
import com.gzoltar.shaded.org.pitest.util.IsolationUtils;
import com.gzoltar.shaded.org.pitest.util.Log;
import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MutationTestWorker {
   private static final Logger LOG = Log.getLogger();
   private static final boolean DEBUG;
   private final Mutater mutater;
   private final ClassLoader loader;
   private final F3<ClassName, ClassLoader, byte[], Boolean> hotswap;

   public MutationTestWorker(F3<ClassName, ClassLoader, byte[], Boolean> hotswap, Mutater mutater, ClassLoader loader) {
      this.loader = loader;
      this.mutater = mutater;
      this.hotswap = hotswap;
   }

   protected void run(Collection<MutationDetails> range, Reporter r, TimeOutDecoratedTestSource testSource) throws IOException {
      Iterator i$ = range.iterator();

      while(i$.hasNext()) {
         MutationDetails mutation = (MutationDetails)i$.next();
         if (DEBUG) {
            LOG.fine("Running mutation " + mutation);
         }

         long t0 = System.currentTimeMillis();
         this.processMutation(r, testSource, mutation);
         if (DEBUG) {
            LOG.fine("processed mutation in " + (System.currentTimeMillis() - t0) + " ms.");
         }
      }

   }

   private void processMutation(Reporter r, TimeOutDecoratedTestSource testSource, MutationDetails mutationDetails) throws IOException {
      MutationIdentifier mutationId = mutationDetails.getId();
      Mutant mutatedClass = this.mutater.getMutation(mutationId);
      JavassistInterceptor.setMutant(mutatedClass);
      if (DEBUG) {
         LOG.fine("mutating method " + mutatedClass.getDetails().getMethod());
      }

      List<TestUnit> relevantTests = testSource.translateTests(mutationDetails.getTestsInOrder());
      r.describe(mutationId);
      MutationStatusTestPair mutationDetected = this.handleMutation(mutationDetails, mutatedClass, relevantTests);
      r.report(mutationId, mutationDetected);
      if (DEBUG) {
         LOG.fine("Mutation " + mutationId + " detected = " + mutationDetected);
      }

   }

   private MutationStatusTestPair handleMutation(MutationDetails mutationId, Mutant mutatedClass, List<TestUnit> relevantTests) {
      MutationStatusTestPair mutationDetected;
      if (relevantTests != null && !relevantTests.isEmpty()) {
         mutationDetected = this.handleCoveredMutation(mutationId, mutatedClass, relevantTests);
      } else {
         LOG.info("No test coverage for mutation  " + mutationId + " in " + mutatedClass.getDetails().getMethod());
         mutationDetected = new MutationStatusTestPair(0, DetectionStatus.RUN_ERROR);
      }

      return mutationDetected;
   }

   private MutationStatusTestPair handleCoveredMutation(MutationDetails mutationId, Mutant mutatedClass, List<TestUnit> relevantTests) {
      if (DEBUG) {
         LOG.fine("" + relevantTests.size() + " relevant test for " + mutatedClass.getDetails().getMethod());
      }

      ClassLoader activeloader = this.pickClassLoaderForMutant(mutationId);
      Container c = createNewContainer(activeloader);
      long t0 = System.currentTimeMillis();
      MutationStatusTestPair mutationDetected;
      if ((Boolean)this.hotswap.apply(mutationId.getClassName(), activeloader, mutatedClass.getBytes())) {
         if (DEBUG) {
            LOG.fine("replaced class with mutant in " + (System.currentTimeMillis() - t0) + " ms");
         }

         mutationDetected = this.doTestsDetectMutation(c, relevantTests);
      } else {
         LOG.warning("Mutation " + mutationId + " was not viable ");
         mutationDetected = new MutationStatusTestPair(0, DetectionStatus.NON_VIABLE);
      }

      return mutationDetected;
   }

   private static Container createNewContainer(final ClassLoader activeloader) {
      Container c = new UnContainer() {
         public List<TestResult> execute(TestUnit group) {
            List<TestResult> results = new ArrayList();
            ExitingResultCollector rc = new ExitingResultCollector(new ConcreteResultCollector(results));
            group.execute(activeloader, rc);
            return results;
         }
      };
      return c;
   }

   private ClassLoader pickClassLoaderForMutant(MutationDetails mutant) {
      if (mutant.mayPoisonJVM()) {
         if (DEBUG) {
            LOG.fine("Creating new classloader for static initializer");
         }

         return new DefaultPITClassloader(new ClassPath(), IsolationUtils.bootClassLoader());
      } else {
         return this.loader;
      }
   }

   public String toString() {
      return "MutationTestWorker [mutater=" + this.mutater + ", loader=" + this.loader + ", hotswap=" + this.hotswap + "]";
   }

   private MutationStatusTestPair doTestsDetectMutation(Container c, List<TestUnit> tests) {
      try {
         CheckTestHasFailedResultListener listener = new CheckTestHasFailedResultListener();
         Pitest pit = new Pitest(Collections.singletonList(listener));
         pit.run(c, this.createEarlyExitTestGroup(tests));
         return this.createStatusTestPair(listener);
      } catch (Exception var5) {
         throw Unchecked.translateCheckedException(var5);
      }
   }

   private MutationStatusTestPair createStatusTestPair(CheckTestHasFailedResultListener listener) {
      return listener.lastFailingTest().hasSome() ? new MutationStatusTestPair(listener.getNumberOfTestsRun(), listener.status(), ((Description)listener.lastFailingTest().value()).getQualifiedName()) : new MutationStatusTestPair(listener.getNumberOfTestsRun(), listener.status());
   }

   private List<TestUnit> createEarlyExitTestGroup(List<TestUnit> tests) {
      return Collections.singletonList(new MultipleTestGroup(tests));
   }

   static {
      DEBUG = LOG.isLoggable(Level.FINE);
   }
}

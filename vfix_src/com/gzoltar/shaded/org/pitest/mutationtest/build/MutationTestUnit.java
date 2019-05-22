package com.gzoltar.shaded.org.pitest.mutationtest.build;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.mutationtest.DetectionStatus;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationMetaData;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationStatusMap;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import com.gzoltar.shaded.org.pitest.mutationtest.execute.MutationTestProcess;
import com.gzoltar.shaded.org.pitest.util.ExitCode;
import com.gzoltar.shaded.org.pitest.util.Log;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

public class MutationTestUnit implements MutationAnalysisUnit {
   private static final Logger LOG = Log.getLogger();
   private final Collection<MutationDetails> availableMutations;
   private final WorkerFactory workerFactory;
   private final Collection<ClassName> testClasses;

   public MutationTestUnit(Collection<MutationDetails> availableMutations, Collection<ClassName> testClasses, WorkerFactory workerFactor) {
      this.availableMutations = availableMutations;
      this.testClasses = testClasses;
      this.workerFactory = workerFactor;
   }

   public MutationMetaData call() throws Exception {
      MutationStatusMap mutations = new MutationStatusMap();
      mutations.setStatusForMutations(this.availableMutations, DetectionStatus.NOT_STARTED);
      mutations.markUncoveredMutations();
      this.runTestsInSeperateProcess(mutations);
      return this.reportResults(mutations);
   }

   private void runTestInSeperateProcessForMutationRange(MutationStatusMap mutations) throws IOException, InterruptedException {
      Collection<MutationDetails> remainingMutations = mutations.getUnrunMutations();
      MutationTestProcess worker = this.workerFactory.createWorker(remainingMutations, this.testClasses);
      worker.start();
      this.setFirstMutationToStatusOfStartedInCaseSlaveFailsAtBoot(mutations, remainingMutations);
      ExitCode exitCode = this.waitForSlaveToDie(worker);
      worker.results(mutations);
      this.correctResultForProcessExitCode(mutations, exitCode);
   }

   private ExitCode waitForSlaveToDie(MutationTestProcess worker) {
      ExitCode exitCode = worker.waitToDie();
      LOG.fine("Exit code was - " + exitCode);
      return exitCode;
   }

   private void setFirstMutationToStatusOfStartedInCaseSlaveFailsAtBoot(MutationStatusMap mutations, Collection<MutationDetails> remainingMutations) {
      mutations.setStatusForMutation((MutationDetails)remainingMutations.iterator().next(), DetectionStatus.STARTED);
   }

   private void correctResultForProcessExitCode(MutationStatusMap mutations, ExitCode exitCode) {
      if (!exitCode.isOk()) {
         Collection<MutationDetails> unfinishedRuns = mutations.getUnfinishedRuns();
         DetectionStatus status = DetectionStatus.getForErrorExitCode(exitCode);
         LOG.warning("Slave exited abnormally due to " + status);
         LOG.fine("Setting " + unfinishedRuns.size() + " unfinished runs to " + status + " state");
         mutations.setStatusForMutations(unfinishedRuns, status);
      } else {
         LOG.fine("Slave exited ok");
      }

   }

   private void runTestsInSeperateProcess(MutationStatusMap mutations) throws IOException, InterruptedException {
      while(mutations.hasUnrunMutations()) {
         this.runTestInSeperateProcessForMutationRange(mutations);
      }

   }

   private MutationMetaData reportResults(MutationStatusMap mutationsMap) {
      return new MutationMetaData(mutationsMap.createMutationResults());
   }

   public int priority() {
      return this.availableMutations.size();
   }
}

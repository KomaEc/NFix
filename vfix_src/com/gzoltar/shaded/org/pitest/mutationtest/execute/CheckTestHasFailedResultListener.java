package com.gzoltar.shaded.org.pitest.mutationtest.execute;

import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.mutationtest.DetectionStatus;
import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.testapi.TestListener;
import com.gzoltar.shaded.org.pitest.testapi.TestResult;

public class CheckTestHasFailedResultListener implements TestListener {
   private Option<Description> lastFailingTest = Option.none();
   private int testsRun = 0;

   public void onTestFailure(TestResult tr) {
      this.lastFailingTest = Option.some(tr.getDescription());
   }

   public void onTestSkipped(TestResult tr) {
   }

   public void onTestStart(Description d) {
      ++this.testsRun;
   }

   public void onTestSuccess(TestResult tr) {
   }

   public DetectionStatus status() {
      return this.lastFailingTest.hasSome() ? DetectionStatus.KILLED : DetectionStatus.SURVIVED;
   }

   public Option<Description> lastFailingTest() {
      return this.lastFailingTest;
   }

   public int getNumberOfTestsRun() {
      return this.testsRun;
   }

   public void onRunEnd() {
   }

   public void onRunStart() {
   }
}

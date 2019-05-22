package com.gzoltar.shaded.org.pitest.junit.adapter;

import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.testapi.ResultCollector;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.StoppedByUserException;

class AdaptingRunListener extends RunListener {
   private final Description description;
   private final ResultCollector rc;
   private boolean failed = false;

   public AdaptingRunListener(Description description, ResultCollector rc) {
      this.description = description;
      this.rc = rc;
   }

   public void testFailure(Failure failure) throws Exception {
      this.rc.notifyEnd(this.description, failure.getException());
      this.failed = true;
   }

   public void testAssumptionFailure(Failure failure) {
   }

   public void testIgnored(org.junit.runner.Description description) throws Exception {
      this.rc.notifySkipped(this.description);
   }

   public void testStarted(org.junit.runner.Description description) throws Exception {
      if (this.failed) {
         throw new StoppedByUserException();
      } else {
         this.rc.notifyStart(this.description);
      }
   }

   public void testFinished(org.junit.runner.Description description) throws Exception {
      if (!this.failed) {
         this.rc.notifyEnd(this.description);
      }

   }
}

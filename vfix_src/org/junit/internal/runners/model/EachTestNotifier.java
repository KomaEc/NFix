package org.junit.internal.runners.model;

import java.util.Iterator;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

public class EachTestNotifier {
   private final RunNotifier notifier;
   private final Description description;

   public EachTestNotifier(RunNotifier notifier, Description description) {
      this.notifier = notifier;
      this.description = description;
   }

   public void addFailure(Throwable targetException) {
      if (targetException instanceof org.junit.runners.model.MultipleFailureException) {
         this.addMultipleFailureException((org.junit.runners.model.MultipleFailureException)targetException);
      } else {
         this.notifier.fireTestFailure(new Failure(this.description, targetException));
      }

   }

   private void addMultipleFailureException(org.junit.runners.model.MultipleFailureException mfe) {
      Iterator i$ = mfe.getFailures().iterator();

      while(i$.hasNext()) {
         Throwable each = (Throwable)i$.next();
         this.addFailure(each);
      }

   }

   public void addFailedAssumption(AssumptionViolatedException e) {
      this.notifier.fireTestAssumptionFailed(new Failure(this.description, e));
   }

   public void fireTestFinished() {
      this.notifier.fireTestFinished(this.description);
   }

   public void fireTestStarted() {
      this.notifier.fireTestStarted(this.description);
   }

   public void fireTestIgnored() {
      this.notifier.fireTestIgnored(this.description);
   }
}

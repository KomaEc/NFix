package org.junit.rules;

import java.util.ArrayList;
import java.util.List;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

public abstract class TestWatcher implements TestRule {
   public Statement apply(final Statement base, final Description description) {
      return new Statement() {
         public void evaluate() throws Throwable {
            List<Throwable> errors = new ArrayList();
            TestWatcher.this.startingQuietly(description, errors);

            try {
               base.evaluate();
               TestWatcher.this.succeededQuietly(description, errors);
            } catch (AssumptionViolatedException var7) {
               errors.add(var7);
               TestWatcher.this.skippedQuietly(var7, description, errors);
            } catch (Throwable var8) {
               errors.add(var8);
               TestWatcher.this.failedQuietly(var8, description, errors);
            } finally {
               TestWatcher.this.finishedQuietly(description, errors);
            }

            MultipleFailureException.assertEmpty(errors);
         }
      };
   }

   private void succeededQuietly(Description description, List<Throwable> errors) {
      try {
         this.succeeded(description);
      } catch (Throwable var4) {
         errors.add(var4);
      }

   }

   private void failedQuietly(Throwable e, Description description, List<Throwable> errors) {
      try {
         this.failed(e, description);
      } catch (Throwable var5) {
         errors.add(var5);
      }

   }

   private void skippedQuietly(AssumptionViolatedException e, Description description, List<Throwable> errors) {
      try {
         if (e instanceof org.junit.AssumptionViolatedException) {
            this.skipped((org.junit.AssumptionViolatedException)e, description);
         } else {
            this.skipped(e, description);
         }
      } catch (Throwable var5) {
         errors.add(var5);
      }

   }

   private void startingQuietly(Description description, List<Throwable> errors) {
      try {
         this.starting(description);
      } catch (Throwable var4) {
         errors.add(var4);
      }

   }

   private void finishedQuietly(Description description, List<Throwable> errors) {
      try {
         this.finished(description);
      } catch (Throwable var4) {
         errors.add(var4);
      }

   }

   protected void succeeded(Description description) {
   }

   protected void failed(Throwable e, Description description) {
   }

   protected void skipped(org.junit.AssumptionViolatedException e, Description description) {
      this.skipped((AssumptionViolatedException)e, description);
   }

   /** @deprecated */
   @Deprecated
   protected void skipped(AssumptionViolatedException e, Description description) {
   }

   protected void starting(Description description) {
   }

   protected void finished(Description description) {
   }
}

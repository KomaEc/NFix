package org.junit.runner.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.junit.runner.Description;
import org.junit.runner.Result;

public class RunNotifier {
   private final List<RunListener> listeners = new CopyOnWriteArrayList();
   private volatile boolean pleaseStop = false;

   public void addListener(RunListener listener) {
      if (listener == null) {
         throw new NullPointerException("Cannot add a null listener");
      } else {
         this.listeners.add(this.wrapIfNotThreadSafe(listener));
      }
   }

   public void removeListener(RunListener listener) {
      if (listener == null) {
         throw new NullPointerException("Cannot remove a null listener");
      } else {
         this.listeners.remove(this.wrapIfNotThreadSafe(listener));
      }
   }

   RunListener wrapIfNotThreadSafe(RunListener listener) {
      return (RunListener)(listener.getClass().isAnnotationPresent(RunListener.ThreadSafe.class) ? listener : new SynchronizedRunListener(listener, this));
   }

   public void fireTestRunStarted(final Description description) {
      (new RunNotifier.SafeNotifier() {
         protected void notifyListener(RunListener each) throws Exception {
            each.testRunStarted(description);
         }
      }).run();
   }

   public void fireTestRunFinished(final Result result) {
      (new RunNotifier.SafeNotifier() {
         protected void notifyListener(RunListener each) throws Exception {
            each.testRunFinished(result);
         }
      }).run();
   }

   public void fireTestStarted(final Description description) throws StoppedByUserException {
      if (this.pleaseStop) {
         throw new StoppedByUserException();
      } else {
         (new RunNotifier.SafeNotifier() {
            protected void notifyListener(RunListener each) throws Exception {
               each.testStarted(description);
            }
         }).run();
      }
   }

   public void fireTestFailure(Failure failure) {
      this.fireTestFailures(this.listeners, Arrays.asList(failure));
   }

   private void fireTestFailures(List<RunListener> listeners, final List<Failure> failures) {
      if (!failures.isEmpty()) {
         (new RunNotifier.SafeNotifier(listeners) {
            protected void notifyListener(RunListener listener) throws Exception {
               Iterator i$ = failures.iterator();

               while(i$.hasNext()) {
                  Failure each = (Failure)i$.next();
                  listener.testFailure(each);
               }

            }
         }).run();
      }

   }

   public void fireTestAssumptionFailed(final Failure failure) {
      (new RunNotifier.SafeNotifier() {
         protected void notifyListener(RunListener each) throws Exception {
            each.testAssumptionFailure(failure);
         }
      }).run();
   }

   public void fireTestIgnored(final Description description) {
      (new RunNotifier.SafeNotifier() {
         protected void notifyListener(RunListener each) throws Exception {
            each.testIgnored(description);
         }
      }).run();
   }

   public void fireTestFinished(final Description description) {
      (new RunNotifier.SafeNotifier() {
         protected void notifyListener(RunListener each) throws Exception {
            each.testFinished(description);
         }
      }).run();
   }

   public void pleaseStop() {
      this.pleaseStop = true;
   }

   public void addFirstListener(RunListener listener) {
      if (listener == null) {
         throw new NullPointerException("Cannot add a null listener");
      } else {
         this.listeners.add(0, this.wrapIfNotThreadSafe(listener));
      }
   }

   private abstract class SafeNotifier {
      private final List<RunListener> currentListeners;

      SafeNotifier() {
         this(RunNotifier.this.listeners);
      }

      SafeNotifier(List<RunListener> currentListeners) {
         this.currentListeners = currentListeners;
      }

      void run() {
         int capacity = this.currentListeners.size();
         ArrayList<RunListener> safeListeners = new ArrayList(capacity);
         ArrayList<Failure> failures = new ArrayList(capacity);
         Iterator i$ = this.currentListeners.iterator();

         while(i$.hasNext()) {
            RunListener listener = (RunListener)i$.next();

            try {
               this.notifyListener(listener);
               safeListeners.add(listener);
            } catch (Exception var7) {
               failures.add(new Failure(Description.TEST_MECHANISM, var7));
            }
         }

         RunNotifier.this.fireTestFailures(safeListeners, failures);
      }

      protected abstract void notifyListener(RunListener var1) throws Exception;
   }
}

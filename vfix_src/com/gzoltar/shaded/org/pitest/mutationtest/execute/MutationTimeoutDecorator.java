package com.gzoltar.shaded.org.pitest.mutationtest.execute;

import com.gzoltar.shaded.org.pitest.extension.common.TestUnitDecorator;
import com.gzoltar.shaded.org.pitest.functional.SideEffect;
import com.gzoltar.shaded.org.pitest.mutationtest.TimeoutLengthStrategy;
import com.gzoltar.shaded.org.pitest.testapi.ResultCollector;
import com.gzoltar.shaded.org.pitest.testapi.TestUnit;
import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class MutationTimeoutDecorator extends TestUnitDecorator {
   private final TimeoutLengthStrategy timeOutStrategy;
   private final SideEffect timeOutSideEffect;
   private final long executionTime;

   public MutationTimeoutDecorator(TestUnit child, SideEffect timeOutSideEffect, TimeoutLengthStrategy timeStrategy, long executionTime) {
      super(child);
      this.timeOutSideEffect = timeOutSideEffect;
      this.executionTime = executionTime;
      this.timeOutStrategy = timeStrategy;
   }

   public void execute(ClassLoader loader, ResultCollector rc) {
      long maxTime = this.timeOutStrategy.getAllowedTime(this.executionTime);
      FutureTask<?> future = this.createFutureForChildTestUnit(loader, rc);
      this.executeFutureWithTimeOut(maxTime, future, rc);
      if (!future.isDone()) {
         this.timeOutSideEffect.apply();
      }

   }

   private void executeFutureWithTimeOut(long maxTime, FutureTask<?> future, ResultCollector rc) {
      try {
         future.get(maxTime, TimeUnit.MILLISECONDS);
      } catch (TimeoutException var6) {
      } catch (InterruptedException var7) {
      } catch (ExecutionException var8) {
         throw Unchecked.translateCheckedException(var8);
      }

   }

   private FutureTask<?> createFutureForChildTestUnit(ClassLoader loader, ResultCollector rc) {
      FutureTask<?> future = new FutureTask(this.createRunnable(loader, rc), (Object)null);
      Thread thread = new Thread(future);
      thread.setDaemon(true);
      thread.setName("mutationTestThread");
      thread.start();
      return future;
   }

   private Runnable createRunnable(final ClassLoader loader, final ResultCollector rc) {
      return new Runnable() {
         public void run() {
            try {
               MutationTimeoutDecorator.this.child().execute(loader, rc);
            } catch (Throwable var2) {
               rc.notifyEnd(MutationTimeoutDecorator.this.child().getDescription(), var2);
            }

         }
      };
   }
}

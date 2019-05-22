package org.testng.internal.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FutureResultAdapter implements IFutureResult {
   Future<?> m_future;

   public FutureResultAdapter(Future<?> future) {
      this.m_future = future;
   }

   public Object get() throws InterruptedException, ThreadExecutionException {
      try {
         return this.m_future.get();
      } catch (ExecutionException var2) {
         throw new ThreadExecutionException(var2.getCause());
      }
   }
}

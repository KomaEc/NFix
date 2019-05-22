package org.testng.internal;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import org.testng.TestNGException;
import org.testng.collections.Lists;

public class PoolService<FutureType> {
   private ExecutorCompletionService<FutureType> m_completionService;
   private ThreadFactory m_threadFactory = new ThreadFactory() {
      private int m_threadIndex = 0;

      public Thread newThread(Runnable r) {
         Thread result = new Thread(r);
         result.setName("PoolService-" + this.m_threadIndex);
         ++this.m_threadIndex;
         return result;
      }
   };
   private ExecutorService m_executor;

   public PoolService(int threadPoolSize) {
      this.m_executor = Executors.newFixedThreadPool(threadPoolSize, this.m_threadFactory);
      this.m_completionService = new ExecutorCompletionService(this.m_executor);
   }

   public List<FutureType> submitTasksAndWait(List<? extends Callable<FutureType>> tasks) {
      List<FutureType> result = Lists.newArrayList();
      Iterator i$ = tasks.iterator();

      while(i$.hasNext()) {
         Callable<FutureType> callable = (Callable)i$.next();
         this.m_completionService.submit(callable);
      }

      for(int i = 0; i < tasks.size(); ++i) {
         try {
            Future<FutureType> take = this.m_completionService.take();
            result.add(take.get());
         } catch (ExecutionException | InterruptedException var5) {
            throw new TestNGException(var5);
         }
      }

      this.m_executor.shutdown();
      return result;
   }
}

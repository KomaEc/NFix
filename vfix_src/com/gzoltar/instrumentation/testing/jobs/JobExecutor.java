package com.gzoltar.instrumentation.testing.jobs;

import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.testing.TestResult;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class JobExecutor {
   private final ExecutorService executor;
   private final List<JobHandler> jobs;

   public JobExecutor(List<JobHandler> var1, int var2) {
      if (var2 == 1) {
         this.executor = Executors.newSingleThreadExecutor();
      } else {
         this.executor = Executors.newFixedThreadPool(var2 == -1 ? Runtime.getRuntime().availableProcessors() : var2);
      }

      this.jobs = var1;
   }

   public List<TestResult> runJobs() {
      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.jobs.iterator();

      while(var3.hasNext()) {
         JobHandler var4 = (JobHandler)var3.next();
         Future var5 = this.executor.submit(var4);
         var2.add(var5);
      }

      this.executor.shutdown();

      while(!this.executor.isTerminated()) {
      }

      var3 = var2.iterator();

      while(var3.hasNext()) {
         Future var7 = (Future)var3.next();

         try {
            TestResult var8 = (TestResult)var7.get();
            if (!var7.isDone()) {
               var7.cancel(true);
            }

            if (var8 != null) {
               var1.add(var8);
            }
         } catch (ExecutionException | InterruptedException var6) {
            Logger.getInstance().err(var6.getMessage(), var6);
         }
      }

      return var1;
   }
}

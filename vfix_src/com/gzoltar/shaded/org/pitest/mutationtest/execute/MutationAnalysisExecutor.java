package com.gzoltar.shaded.org.pitest.mutationtest.execute;

import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import com.gzoltar.shaded.org.pitest.mutationtest.ClassMutationResults;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationMetaData;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListener;
import com.gzoltar.shaded.org.pitest.mutationtest.build.MutationAnalysisUnit;
import com.gzoltar.shaded.org.pitest.util.Log;
import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MutationAnalysisExecutor {
   private static final Logger LOG = Log.getLogger();
   private final List<MutationResultListener> listeners;
   private final ThreadPoolExecutor executor;

   public MutationAnalysisExecutor(int numberOfThreads, List<MutationResultListener> listeners) {
      this.listeners = listeners;
      this.executor = new ThreadPoolExecutor(numberOfThreads, numberOfThreads, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue(), Executors.defaultThreadFactory());
   }

   public void run(List<MutationAnalysisUnit> testUnits) {
      LOG.fine("Running " + testUnits.size() + " units");
      this.signalRunStartToAllListeners();
      List<Future<MutationMetaData>> results = new ArrayList(testUnits.size());
      Iterator i$ = testUnits.iterator();

      while(i$.hasNext()) {
         MutationAnalysisUnit unit = (MutationAnalysisUnit)i$.next();
         results.add(this.executor.submit(unit));
      }

      this.executor.shutdown();

      try {
         this.processResult(results);
      } catch (InterruptedException var5) {
         throw Unchecked.translateCheckedException(var5);
      } catch (ExecutionException var6) {
         throw Unchecked.translateCheckedException(var6);
      }

      this.signalRunEndToAllListeners();
   }

   private void processResult(List<Future<MutationMetaData>> results) throws InterruptedException, ExecutionException {
      Iterator i$ = results.iterator();

      while(i$.hasNext()) {
         Future<MutationMetaData> f = (Future)i$.next();
         MutationMetaData r = (MutationMetaData)f.get();
         Iterator i$ = this.listeners.iterator();

         while(i$.hasNext()) {
            MutationResultListener l = (MutationResultListener)i$.next();
            Iterator i$ = r.toClassResults().iterator();

            while(i$.hasNext()) {
               ClassMutationResults cr = (ClassMutationResults)i$.next();
               l.handleMutationResult(cr);
            }
         }
      }

   }

   private void signalRunStartToAllListeners() {
      FCollection.forEach(this.listeners, new SideEffect1<MutationResultListener>() {
         public void apply(MutationResultListener a) {
            a.runStart();
         }
      });
   }

   private void signalRunEndToAllListeners() {
      FCollection.forEach(this.listeners, new SideEffect1<MutationResultListener>() {
         public void apply(MutationResultListener a) {
            a.runEnd();
         }
      });
   }
}

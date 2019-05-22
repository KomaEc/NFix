package com.gzoltar.shaded.org.pitest.mutationtest.incremental;

import com.gzoltar.shaded.org.pitest.mutationtest.ClassMutationResults;
import com.gzoltar.shaded.org.pitest.mutationtest.HistoryStore;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResult;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListener;
import java.util.Iterator;

public class HistoryListener implements MutationResultListener {
   private final HistoryStore historyStore;

   public HistoryListener(HistoryStore historyStore) {
      this.historyStore = historyStore;
   }

   public void runStart() {
   }

   public void handleMutationResult(ClassMutationResults metaData) {
      Iterator i$ = metaData.getMutations().iterator();

      while(i$.hasNext()) {
         MutationResult each = (MutationResult)i$.next();
         this.historyStore.recordResult(each);
      }

   }

   public void runEnd() {
   }
}

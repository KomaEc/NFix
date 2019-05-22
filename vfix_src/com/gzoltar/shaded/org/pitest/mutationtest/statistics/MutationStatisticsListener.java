package com.gzoltar.shaded.org.pitest.mutationtest.statistics;

import com.gzoltar.shaded.org.pitest.mutationtest.ClassMutationResults;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListener;

public class MutationStatisticsListener implements MutationResultListener, MutationStatisticsSource {
   private final MutationStatistics mutatorScores = new MutationStatistics();

   public MutationStatistics getStatistics() {
      return this.mutatorScores;
   }

   public void runStart() {
   }

   public void handleMutationResult(ClassMutationResults metaData) {
      this.processMetaData(metaData);
   }

   public void runEnd() {
   }

   private void processMetaData(ClassMutationResults value) {
      this.mutatorScores.registerResults(value.getMutations());
   }
}

package com.gzoltar.shaded.org.pitest.mutationtest.build;

import com.gzoltar.shaded.org.pitest.mutationtest.MutationMetaData;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResult;
import com.gzoltar.shaded.org.pitest.util.Log;
import java.util.List;
import java.util.logging.Logger;

public class KnownStatusMutationTestUnit implements MutationAnalysisUnit {
   private static final Logger LOG = Log.getLogger();
   private final List<MutationResult> mutations;

   public KnownStatusMutationTestUnit(List<MutationResult> mutations) {
      this.mutations = mutations;
   }

   public MutationMetaData call() throws Exception {
      LOG.fine("Using historic results for " + this.mutations.size() + " mutations");
      return new MutationMetaData(this.mutations);
   }

   public int priority() {
      return Integer.MAX_VALUE;
   }
}

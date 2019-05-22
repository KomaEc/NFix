package com.gzoltar.shaded.org.pitest.mutationtest.build;

import com.gzoltar.shaded.org.pitest.classinfo.ClassByteArraySource;
import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationConfig;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.Mutater;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import com.gzoltar.shaded.org.pitest.mutationtest.filter.MutationFilter;
import com.gzoltar.shaded.org.pitest.util.Log;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class MutationSource {
   private static final Logger LOG = Log.getLogger();
   private final MutationConfig mutationConfig;
   private final TestPrioritiser testPrioritiser;
   private final MutationFilter filter;
   private final ClassByteArraySource source;

   public MutationSource(MutationConfig mutationConfig, MutationFilter filter, TestPrioritiser testPrioritiser, ClassByteArraySource source) {
      this.mutationConfig = mutationConfig;
      this.testPrioritiser = testPrioritiser;
      this.filter = filter;
      this.source = source;
   }

   public Collection<MutationDetails> createMutations(ClassName clazz) {
      Mutater m = this.mutationConfig.createMutator(this.source);
      Collection<MutationDetails> availableMutations = this.filter.filter(m.findMutations(clazz));
      this.assignTestsToMutations(availableMutations);
      return availableMutations;
   }

   private void assignTestsToMutations(Collection<MutationDetails> availableMutations) {
      MutationDetails mutation;
      List testDetails;
      for(Iterator i$ = availableMutations.iterator(); i$.hasNext(); mutation.addTestsInOrder(testDetails)) {
         mutation = (MutationDetails)i$.next();
         testDetails = this.testPrioritiser.assignTests(mutation);
         if (testDetails.isEmpty()) {
            LOG.fine("According to coverage no tests hit the mutation " + mutation);
         }
      }

   }
}

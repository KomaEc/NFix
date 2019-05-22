package com.gzoltar.shaded.org.pitest.mutationtest.filter;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class LimitNumberOfMutationPerClassFilter implements MutationFilter {
   private final int maxMutationsPerClass;

   public LimitNumberOfMutationPerClassFilter(int max) {
      this.maxMutationsPerClass = max;
   }

   public Collection<MutationDetails> filter(Collection<MutationDetails> mutations) {
      return mutations.size() <= this.maxMutationsPerClass ? mutations : this.createEvenlyDistributedSampling(mutations);
   }

   private Collection<MutationDetails> createEvenlyDistributedSampling(Collection<MutationDetails> mutations) {
      Collection<MutationDetails> filtered = new ArrayList(this.maxMutationsPerClass);
      int step = mutations.size() / this.maxMutationsPerClass;
      Iterator it = mutations.iterator();

      while(it.hasNext()) {
         int i = 0;

         MutationDetails value;
         for(value = null; it.hasNext() && i != step; ++i) {
            value = (MutationDetails)it.next();
         }

         if (filtered.size() != this.maxMutationsPerClass) {
            filtered.add(value);
         }
      }

      return filtered;
   }
}

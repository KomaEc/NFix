package com.gzoltar.shaded.org.pitest.mutationtest.filter;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import java.util.Collection;

public enum UnfilteredMutationFilter implements MutationFilter {
   INSTANCE;

   public Collection<MutationDetails> filter(Collection<MutationDetails> mutations) {
      return mutations;
   }
}

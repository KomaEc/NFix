package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.inlinedcode;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import java.util.Collection;

public class NoInlinedCodeDetection implements InlinedCodeFilter {
   public Collection<MutationDetails> process(Collection<MutationDetails> mutations) {
      return mutations;
   }
}

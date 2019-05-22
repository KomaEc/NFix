package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.inlinedcode;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import java.util.Collection;

public interface InlinedCodeFilter {
   Collection<MutationDetails> process(Collection<MutationDetails> var1);
}

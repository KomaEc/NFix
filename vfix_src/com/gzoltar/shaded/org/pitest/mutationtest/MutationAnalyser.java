package com.gzoltar.shaded.org.pitest.mutationtest;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import java.util.Collection;

public interface MutationAnalyser {
   Collection<MutationResult> analyse(Collection<MutationDetails> var1);
}

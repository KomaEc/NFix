package com.gzoltar.shaded.org.pitest.mutationtest.filter;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import java.util.Collection;

public interface MutationFilter {
   Collection<MutationDetails> filter(Collection<MutationDetails> var1);
}

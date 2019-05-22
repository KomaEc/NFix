package com.gzoltar.shaded.org.pitest.mutationtest.build;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import java.util.Collection;
import java.util.List;

public interface MutationGrouper {
   List<List<MutationDetails>> groupMutations(Collection<ClassName> var1, Collection<MutationDetails> var2);
}

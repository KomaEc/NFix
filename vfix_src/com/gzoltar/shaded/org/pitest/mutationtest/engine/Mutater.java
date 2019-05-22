package com.gzoltar.shaded.org.pitest.mutationtest.engine;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import java.util.List;

public interface Mutater {
   Mutant getMutation(MutationIdentifier var1);

   List<MutationDetails> findMutations(ClassName var1);
}

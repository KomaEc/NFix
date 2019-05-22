package com.gzoltar.shaded.org.pitest.mutationtest.engine;

import com.gzoltar.shaded.org.pitest.classinfo.ClassByteArraySource;
import java.util.Collection;

public interface MutationEngine {
   Mutater createMutator(ClassByteArraySource var1);

   Collection<String> getMutatorNames();
}

package com.gzoltar.shaded.org.pitest.mutationtest.filter;

import com.gzoltar.shaded.org.pitest.classpath.CodeSource;
import java.util.Properties;

public class LimitNumberOfMutationsPerClassFilterFactory implements MutationFilterFactory {
   public MutationFilter createFilter(Properties props, CodeSource source, int maxMutationsPerClass) {
      return (MutationFilter)(maxMutationsPerClass > 0 ? new LimitNumberOfMutationPerClassFilter(maxMutationsPerClass) : UnfilteredMutationFilter.INSTANCE);
   }

   public String description() {
      return "Default limit mutations plugin";
   }
}

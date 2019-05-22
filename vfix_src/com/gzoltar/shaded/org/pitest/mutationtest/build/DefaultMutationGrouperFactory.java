package com.gzoltar.shaded.org.pitest.mutationtest.build;

import com.gzoltar.shaded.org.pitest.classpath.CodeSource;
import java.util.Properties;

public class DefaultMutationGrouperFactory implements MutationGrouperFactory {
   public String description() {
      return "Default mutation grouping";
   }

   public MutationGrouper makeFactory(Properties props, CodeSource codeSource, int numberOfThreads, int unitSize) {
      return new DefaultGrouper(unitSize);
   }
}

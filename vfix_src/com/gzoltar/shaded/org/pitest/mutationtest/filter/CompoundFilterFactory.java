package com.gzoltar.shaded.org.pitest.mutationtest.filter;

import com.gzoltar.shaded.org.pitest.classpath.CodeSource;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class CompoundFilterFactory implements MutationFilterFactory {
   private final List<MutationFilterFactory> children = new ArrayList();

   public CompoundFilterFactory(Collection<? extends MutationFilterFactory> filters) {
      this.children.addAll(filters);
   }

   public String description() {
      return null;
   }

   public MutationFilter createFilter(Properties props, CodeSource source, int maxMutationsPerClass) {
      List<MutationFilter> filters = FCollection.map(this.children, toFilter(props, source, maxMutationsPerClass));
      return new CompoundMutationFilter(filters);
   }

   private static F<MutationFilterFactory, MutationFilter> toFilter(final Properties props, final CodeSource source, final int maxMutationsPerClass) {
      return new F<MutationFilterFactory, MutationFilter>() {
         public MutationFilter apply(MutationFilterFactory a) {
            return a.createFilter(props, source, maxMutationsPerClass);
         }
      };
   }
}

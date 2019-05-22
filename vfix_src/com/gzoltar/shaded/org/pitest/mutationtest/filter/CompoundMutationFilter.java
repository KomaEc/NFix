package com.gzoltar.shaded.org.pitest.mutationtest.filter;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

class CompoundMutationFilter implements MutationFilter {
   private final List<MutationFilter> children = new ArrayList();

   CompoundMutationFilter(List<MutationFilter> children) {
      this.children.addAll(children);
   }

   public Collection<MutationDetails> filter(Collection<MutationDetails> mutations) {
      Collection<MutationDetails> modified = mutations;

      MutationFilter each;
      for(Iterator i$ = this.children.iterator(); i$.hasNext(); modified = each.filter(modified)) {
         each = (MutationFilter)i$.next();
      }

      return modified;
   }
}

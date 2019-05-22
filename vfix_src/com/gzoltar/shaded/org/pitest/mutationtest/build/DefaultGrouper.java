package com.gzoltar.shaded.org.pitest.mutationtest.build;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DefaultGrouper implements MutationGrouper {
   private final int unitSize;

   public DefaultGrouper(int unitSize) {
      this.unitSize = unitSize;
   }

   public List<List<MutationDetails>> groupMutations(Collection<ClassName> codeClasses, Collection<MutationDetails> mutations) {
      Map<ClassName, Collection<MutationDetails>> bucketed = FCollection.bucket(mutations, byClass());
      List<List<MutationDetails>> chunked = new ArrayList();
      Iterator i$ = bucketed.values().iterator();

      while(i$.hasNext()) {
         Collection<MutationDetails> each = (Collection)i$.next();
         this.shrinkToMaximumUnitSize(chunked, each);
      }

      return chunked;
   }

   private void shrinkToMaximumUnitSize(List<List<MutationDetails>> chunked, Collection<MutationDetails> each) {
      if (this.unitSize > 0) {
         Iterator i$ = FCollection.splitToLength(this.unitSize, each).iterator();

         while(i$.hasNext()) {
            List<MutationDetails> ms = (List)i$.next();
            chunked.add(ms);
         }
      } else {
         chunked.add(new ArrayList(each));
      }

   }

   private static F<MutationDetails, ClassName> byClass() {
      return new F<MutationDetails, ClassName>() {
         public ClassName apply(MutationDetails a) {
            return a.getClassName();
         }
      };
   }
}

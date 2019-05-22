package com.gzoltar.shaded.org.pitest.mutationtest.report.html;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.FunctionalIterable;
import com.gzoltar.shaded.org.pitest.functional.FunctionalList;
import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class MutationResultList implements FunctionalIterable<MutationResult> {
   private final List<MutationResult> impl = new ArrayList();

   public MutationResultList(Collection<MutationResult> results) {
      this.impl.addAll(results);
   }

   public List<MutationGrouping> groupMutationsByLine() {
      this.sortMutationsIntoLineOrder();
      List<MutationGrouping> groups = new ArrayList();
      List<MutationResult> sublist = new ArrayList();
      int lastLineNumber = -1;

      MutationResult each;
      for(Iterator i$ = this.impl.iterator(); i$.hasNext(); lastLineNumber = each.getDetails().getLineNumber()) {
         each = (MutationResult)i$.next();
         if (lastLineNumber != each.getDetails().getLineNumber() && !sublist.isEmpty()) {
            groups.add(new MutationGrouping(lastLineNumber, "Line " + lastLineNumber, sublist));
            sublist = new ArrayList();
         }

         sublist.add(each);
      }

      if (!sublist.isEmpty()) {
         groups.add(new MutationGrouping(lastLineNumber, "Line " + lastLineNumber, sublist));
      }

      return groups;
   }

   private void sortMutationsIntoLineOrder() {
      Comparator<MutationResult> c = new Comparator<MutationResult>() {
         public int compare(MutationResult o1, MutationResult o2) {
            return o1.getDetails().getLineNumber() - o2.getDetails().getLineNumber();
         }
      };
      Collections.sort(this.impl, c);
   }

   public boolean contains(F<MutationResult, Boolean> predicate) {
      return FCollection.contains(this.impl, predicate);
   }

   public FunctionalList<MutationResult> filter(F<MutationResult, Boolean> predicate) {
      return FCollection.filter(this, predicate);
   }

   public <B> FunctionalList<B> flatMap(F<MutationResult, ? extends Iterable<B>> f) {
      return FCollection.flatMap(this, f);
   }

   public void forEach(SideEffect1<MutationResult> e) {
      FCollection.forEach(this, e);
   }

   public Iterator<MutationResult> iterator() {
      return this.impl.iterator();
   }

   public <B> FunctionalList<B> map(F<MutationResult, B> f) {
      return FCollection.map(this, f);
   }

   public <B> void mapTo(F<MutationResult, B> f, Collection<? super B> bs) {
      FCollection.mapTo(this, f, bs);
   }
}

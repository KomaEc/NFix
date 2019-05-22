package com.gzoltar.shaded.org.pitest.functional.predicate;

import com.gzoltar.shaded.org.pitest.functional.F;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class And<A> implements Predicate<A> {
   private final Set<F<A, Boolean>> ps = new LinkedHashSet();

   public And(Iterable<? extends F<A, Boolean>> ps) {
      Iterator i$ = ps.iterator();

      while(i$.hasNext()) {
         F<A, Boolean> each = (F)i$.next();
         this.ps.add(each);
      }

   }

   public Boolean apply(A a) {
      Iterator i$ = this.ps.iterator();

      F each;
      do {
         if (!i$.hasNext()) {
            return !this.ps.isEmpty();
         }

         each = (F)i$.next();
      } while((Boolean)each.apply(a));

      return false;
   }
}

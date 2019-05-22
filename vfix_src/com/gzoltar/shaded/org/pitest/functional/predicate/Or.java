package com.gzoltar.shaded.org.pitest.functional.predicate;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Or<A> implements Predicate<A> {
   private final Set<Predicate<A>> ps = new LinkedHashSet();

   public Or(Iterable<Predicate<A>> ps) {
      Iterator i$ = ps.iterator();

      while(i$.hasNext()) {
         Predicate<A> each = (Predicate)i$.next();
         this.ps.add(each);
      }

   }

   public Boolean apply(A a) {
      Iterator i$ = this.ps.iterator();

      Predicate each;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         each = (Predicate)i$.next();
      } while(!(Boolean)each.apply(a));

      return true;
   }
}

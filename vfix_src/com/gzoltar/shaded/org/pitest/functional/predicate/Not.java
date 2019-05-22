package com.gzoltar.shaded.org.pitest.functional.predicate;

import com.gzoltar.shaded.org.pitest.functional.F;

public final class Not<A> implements Predicate<A> {
   private final F<A, Boolean> p;

   public Not(F<A, Boolean> p) {
      this.p = p;
   }

   public Boolean apply(A a) {
      return !(Boolean)this.p.apply(a);
   }
}

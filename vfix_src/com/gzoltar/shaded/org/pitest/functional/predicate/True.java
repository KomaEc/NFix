package com.gzoltar.shaded.org.pitest.functional.predicate;

public class True<A> implements Predicate<A> {
   private static final True<?> INSTANCE = new True();

   public static <A> Predicate<A> all() {
      return INSTANCE;
   }

   public Boolean apply(A a) {
      return true;
   }
}

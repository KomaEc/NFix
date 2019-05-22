package com.gzoltar.shaded.org.pitest.functional.predicate;

public class False<A> implements Predicate<A> {
   private static final False<?> INSTANCE = new False();

   public static <A> False<A> instance() {
      return INSTANCE;
   }

   public Boolean apply(A a) {
      return false;
   }
}

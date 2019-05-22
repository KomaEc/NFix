package com.gzoltar.shaded.org.pitest.classpath;

import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;

public class ClassFilter {
   private final Predicate<String> test;
   private final Predicate<String> code;

   public ClassFilter(Predicate<String> test, Predicate<String> code) {
      this.test = test;
      this.code = code;
   }

   public Predicate<String> getTest() {
      return this.test;
   }

   public Predicate<String> getCode() {
      return this.code;
   }
}

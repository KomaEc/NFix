package com.gzoltar.shaded.org.pitest.classpath;

import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;

public class PathFilter {
   private final Predicate<ClassPathRoot> codeFilter;
   private final Predicate<ClassPathRoot> testFilter;

   public PathFilter(Predicate<ClassPathRoot> codeFilter, Predicate<ClassPathRoot> testFilter) {
      this.codeFilter = codeFilter;
      this.testFilter = testFilter;
   }

   public Predicate<ClassPathRoot> getCodeFilter() {
      return this.codeFilter;
   }

   public Predicate<ClassPathRoot> getTestFilter() {
      return this.testFilter;
   }
}

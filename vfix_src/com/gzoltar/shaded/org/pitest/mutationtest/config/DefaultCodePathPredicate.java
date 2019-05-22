package com.gzoltar.shaded.org.pitest.mutationtest.config;

import com.gzoltar.shaded.org.pitest.classpath.ClassPathRoot;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;

public class DefaultCodePathPredicate implements Predicate<ClassPathRoot> {
   public Boolean apply(ClassPathRoot a) {
      return a.cacheLocation().hasSome() && !this.isATestPath((String)a.cacheLocation().value()) && !this.isADependencyPath((String)a.cacheLocation().value());
   }

   private boolean isADependencyPath(String path) {
      String lowerCasePath = path.toLowerCase();
      return lowerCasePath.endsWith(".jar") || lowerCasePath.endsWith(".zip");
   }

   private boolean isATestPath(String path) {
      return path.endsWith("test-classes") || path.endsWith("bin-test");
   }
}

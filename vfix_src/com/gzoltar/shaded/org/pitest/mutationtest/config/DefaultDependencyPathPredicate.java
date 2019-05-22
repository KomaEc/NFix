package com.gzoltar.shaded.org.pitest.mutationtest.config;

import com.gzoltar.shaded.org.pitest.classpath.ClassPathRoot;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;

public class DefaultDependencyPathPredicate implements Predicate<ClassPathRoot> {
   public Boolean apply(ClassPathRoot a) {
      return a.cacheLocation().hasSome() && this.isADependencyPath((String)a.cacheLocation().value());
   }

   private boolean isADependencyPath(String path) {
      String lowerCasePath = path.toLowerCase();
      return lowerCasePath.endsWith(".jar") || lowerCasePath.endsWith(".zip");
   }
}

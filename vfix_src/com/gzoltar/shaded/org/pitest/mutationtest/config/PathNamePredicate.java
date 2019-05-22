package com.gzoltar.shaded.org.pitest.mutationtest.config;

import com.gzoltar.shaded.org.pitest.classpath.ClassPathRoot;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;

public class PathNamePredicate implements Predicate<ClassPathRoot> {
   private final Predicate<String> stringFilter;

   public PathNamePredicate(Predicate<String> stringFilter) {
      this.stringFilter = stringFilter;
   }

   public Boolean apply(ClassPathRoot classPathRoot) {
      return this.cacheLocationOptionExists(classPathRoot) && this.cacheLocationMatchesFilter(classPathRoot);
   }

   private Boolean cacheLocationMatchesFilter(ClassPathRoot classPathRoot) {
      String cacheLocationValue = (String)classPathRoot.cacheLocation().value();
      return (Boolean)this.stringFilter.apply(cacheLocationValue);
   }

   private boolean cacheLocationOptionExists(ClassPathRoot a) {
      return a.cacheLocation().hasSome();
   }
}

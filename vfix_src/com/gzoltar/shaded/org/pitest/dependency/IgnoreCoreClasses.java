package com.gzoltar.shaded.org.pitest.dependency;

import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.functional.prelude.Prelude;
import com.gzoltar.shaded.org.pitest.util.Glob;
import java.util.Arrays;
import java.util.Collection;

public class IgnoreCoreClasses implements Predicate<DependencyAccess> {
   private final Predicate<String> impl;
   private final Collection<String> filtered = Arrays.asList("java.*", "sun.*", "javax.*", "org.junit.*", "junit.*", "org.mockito.*", "org.powermock.*", "org.jmock.*", "com.sun.*");

   IgnoreCoreClasses() {
      this.impl = Prelude.not(Prelude.or((Iterable)Glob.toGlobPredicates(this.filtered)));
   }

   public Boolean apply(DependencyAccess a) {
      String owner = a.getDest().getOwner().replace("/", ".");
      return (Boolean)this.impl.apply(owner);
   }
}

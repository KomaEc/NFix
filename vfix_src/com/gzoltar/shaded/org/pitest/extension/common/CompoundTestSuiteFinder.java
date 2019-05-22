package com.gzoltar.shaded.org.pitest.extension.common;

import com.gzoltar.shaded.org.pitest.testapi.TestSuiteFinder;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class CompoundTestSuiteFinder implements TestSuiteFinder {
   private final Collection<TestSuiteFinder> children;

   public CompoundTestSuiteFinder(Collection<TestSuiteFinder> children) {
      this.children = children;
   }

   public List<Class<?>> apply(Class<?> a) {
      Iterator i$ = this.children.iterator();

      List found;
      do {
         if (!i$.hasNext()) {
            return Collections.emptyList();
         }

         TestSuiteFinder i = (TestSuiteFinder)i$.next();
         found = (List)i.apply(a);
      } while(found.isEmpty());

      return found;
   }
}

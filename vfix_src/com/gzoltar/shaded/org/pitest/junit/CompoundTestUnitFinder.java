package com.gzoltar.shaded.org.pitest.junit;

import com.gzoltar.shaded.org.pitest.testapi.TestUnit;
import com.gzoltar.shaded.org.pitest.testapi.TestUnitFinder;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CompoundTestUnitFinder implements TestUnitFinder {
   private final List<TestUnitFinder> tufs;

   public CompoundTestUnitFinder(List<TestUnitFinder> tufs) {
      this.tufs = tufs;
   }

   public List<TestUnit> findTestUnits(Class<?> clazz) {
      Iterator i$ = this.tufs.iterator();

      List tus;
      do {
         if (!i$.hasNext()) {
            return Collections.emptyList();
         }

         TestUnitFinder each = (TestUnitFinder)i$.next();
         tus = each.findTestUnits(clazz);
      } while(tus.isEmpty());

      return tus;
   }
}

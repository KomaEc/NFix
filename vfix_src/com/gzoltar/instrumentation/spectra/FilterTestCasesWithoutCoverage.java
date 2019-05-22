package com.gzoltar.instrumentation.spectra;

import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.testing.TestResult;
import java.util.Iterator;
import java.util.Map;

public class FilterTestCasesWithoutCoverage implements SpectraFilter {
   public void filter(Spectra var1) {
      Map var4;
      Iterator var2 = (var4 = var1.getTestResultsMap()).values().iterator();
      Logger.getInstance().debug("Number of tests before removing " + var4.size());

      while(var2.hasNext()) {
         TestResult var3;
         if ((var3 = (TestResult)var2.next()).getCoveredComponents().isEmpty()) {
            Logger.getInstance().debug("Removing test case " + var3.getName());
            Logger.getInstance().info(":>" + var3.getName() + "<:");
            var2.remove();
         }
      }

      Logger.getInstance().debug("Number of tests after removing " + var4.size());
   }
}

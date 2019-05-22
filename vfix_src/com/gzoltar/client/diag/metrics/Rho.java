package com.gzoltar.client.diag.metrics;

import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.instrumentation.testing.TestResult;
import java.util.Iterator;

public class Rho implements Metrics {
   public double getMetricValue(Spectra var1) {
      if (var1.getNumberOfComponents() == 0) {
         return Double.NaN;
      } else {
         int var2 = 0;

         TestResult var4;
         for(Iterator var3 = var1.getTestResults().iterator(); var3.hasNext(); var2 += var4.getCoveredComponents().size()) {
            var4 = (TestResult)var3.next();
         }

         return (double)var2 / (double)var1.getNumberOfComponents() / (double)var1.getNumberOfTestResults();
      }
   }
}

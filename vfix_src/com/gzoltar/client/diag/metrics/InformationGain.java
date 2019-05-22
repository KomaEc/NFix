package com.gzoltar.client.diag.metrics;

import com.gzoltar.instrumentation.spectra.Spectra;

public class InformationGain implements Metrics {
   public double getMetricValue(Spectra var1) {
      double var2;
      if ((var2 = (new Rho()).getMetricValue(var1)) == Double.NaN) {
         return Double.NaN;
      } else {
         return var2 != 0.0D && var2 != 1.0D ? var2 * -1.0D * this.log2(var2) - (1.0D - var2) * this.log2(1.0D - var2) : 0.0D;
      }
   }
}

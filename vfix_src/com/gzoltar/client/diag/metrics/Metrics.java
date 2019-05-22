package com.gzoltar.client.diag.metrics;

import com.gzoltar.instrumentation.spectra.Spectra;

public interface Metrics {
   double getMetricValue(Spectra var1);

   default double log2(double var1) {
      return Math.log(var1) / Math.log(2.0D);
   }
}

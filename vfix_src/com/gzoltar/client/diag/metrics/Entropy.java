package com.gzoltar.client.diag.metrics;

import com.gzoltar.client.Properties;
import com.gzoltar.instrumentation.components.ComponentCount;
import com.gzoltar.instrumentation.spectra.Spectra;
import java.util.Iterator;

public class Entropy implements Metrics {
   private final String coefficient;

   public Entropy(String var1) {
      this.coefficient = var1;
   }

   public double getMetricValue(Spectra var1) {
      if (!var1.hasFailingTestCases() && !this.coefficient.equals(Properties.Coefficient.BARINEL_SR.name())) {
         return Double.NaN;
      } else {
         double var2 = 0.0D;
         Iterator var5 = var1.getComponents().iterator();

         while(var5.hasNext()) {
            ComponentCount var4;
            if ((var4 = (ComponentCount)var5.next()).getComponent().getSuspiciousnessValue(this.coefficient) != 0.0D) {
               var2 += var4.getComponent().getSuspiciousnessValue(this.coefficient) * this.log2(var4.getComponent().getSuspiciousnessValue(this.coefficient));
            }
         }

         return var2 * -1.0D;
      }
   }
}

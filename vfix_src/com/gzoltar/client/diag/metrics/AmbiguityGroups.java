package com.gzoltar.client.diag.metrics;

import com.gzoltar.instrumentation.components.ComponentCount;
import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.instrumentation.testing.TestResult;
import java.util.HashMap;
import java.util.Iterator;

public class AmbiguityGroups implements Metrics {
   public double getMetricValue(Spectra var1) {
      if (var1.getNumberOfComponents() == 0) {
         return Double.NaN;
      } else {
         HashMap var2 = new HashMap();
         Iterator var3 = var1.getComponents().iterator();

         while(var3.hasNext()) {
            ComponentCount var4 = (ComponentCount)var3.next();
            StringBuilder var5 = new StringBuilder();
            Iterator var6 = var1.getTestResults().iterator();

            while(var6.hasNext()) {
               if (((TestResult)var6.next()).covers(var4)) {
                  var5.append("1");
               } else {
                  var5.append("0");
               }
            }

            if (!var2.containsKey(var5.toString())) {
               var2.put(var5.toString(), 1);
            } else {
               var2.put(var5.toString(), (Integer)var2.get(var5.toString()) + 1);
            }
         }

         double var8 = 0.0D;
         Iterator var12 = var2.keySet().iterator();

         while(var12.hasNext()) {
            String var13 = (String)var12.next();
            double var10;
            if ((var10 = (double)(Integer)var2.get(var13)) != 1.0D) {
               var8 += var10 / (double)var1.getNumberOfComponents() * ((var10 - 1.0D) / 2.0D);
            }
         }

         return var8;
      }
   }
}

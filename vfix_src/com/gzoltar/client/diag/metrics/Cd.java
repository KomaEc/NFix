package com.gzoltar.client.diag.metrics;

import com.gzoltar.client.Properties;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.ComponentCount;
import com.gzoltar.instrumentation.spectra.Spectra;
import java.util.Iterator;
import java.util.List;

public class Cd implements Metrics {
   private final String coefficient;

   public Cd(String var1) {
      this.coefficient = var1;
   }

   public double getMetricValue(Spectra var1) {
      if (Properties.FAULTY_COMPONENTS == null) {
         Logger.getInstance().err("The list of faulty components has to be specified.");
         return Double.NaN;
      } else if (!var1.hasFailingTestCases() && !this.coefficient.equals(Properties.Coefficient.BARINEL_SR.name())) {
         return Double.NaN;
      } else {
         List var2 = var1.getComponentsOrderedBySuspiciousness(this.coefficient);
         double var3 = Double.MIN_VALUE;
         Iterator var5 = var2.iterator();

         while(var5.hasNext()) {
            ComponentCount var6 = (ComponentCount)var5.next();
            String var7 = var6.getComponent().getClassLabel() + "#" + var6.getComponent().getLineNumberLabel();
            String[] var8;
            int var9 = (var8 = Properties.FAULTY_COMPONENTS.split(":")).length;

            for(int var10 = 0; var10 < var9; ++var10) {
               String var11 = var8[var10];
               if (var7.equals(var11)) {
                  var3 = var6.getComponent().getSuspiciousnessValue(this.coefficient);
                  break;
               }
            }

            if (var3 != Double.MIN_VALUE) {
               Logger.getInstance().debug("Suspiciousness of the first faulty component " + var3);
               break;
            }
         }

         if (var3 == Double.MIN_VALUE) {
            Logger.getInstance().warn("There is no faulty component(s) described as " + Properties.FAULTY_COMPONENTS);
         }

         int var14 = 0;
         int var15 = 0;
         Iterator var16 = var2.iterator();

         while(var16.hasNext()) {
            ComponentCount var17;
            if ((var17 = (ComponentCount)var16.next()).getComponent().getSuspiciousnessValue(this.coefficient) > var3) {
               ++var14;
               ++var15;
            } else if (var17.getComponent().getSuspiciousnessValue(this.coefficient) == var3) {
               ++var15;
            }
         }

         double var12 = (double)(var14 + var15 - Properties.FAULTY_COMPONENTS.split(":").length) / 2.0D;

         assert var12 >= 0.0D;

         assert var12 < (double)var1.getNumberOfComponents();

         return var12;
      }
   }
}

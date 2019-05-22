package com.gzoltar.client.diag.metrics;

import com.gzoltar.client.Properties;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.Component;
import com.gzoltar.instrumentation.components.ComponentCount;
import com.gzoltar.instrumentation.spectra.Spectra;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Expense implements Metrics {
   private final String coefficient;

   public Expense(String var1) {
      this.coefficient = var1;
   }

   public double getMetricValue(Spectra var1) {
      if (Properties.FAULTY_COMPONENTS == null) {
         Logger.getInstance().err("The list of faulty components has to be specified.");
         return Double.NaN;
      } else if (!var1.hasFailingTestCases() && !this.coefficient.equals(Properties.Coefficient.BARINEL_SR.name())) {
         return Double.NaN;
      } else {
         HashMap var2 = new HashMap();
         Iterator var3 = var1.getComponentsOrderedBySuspiciousness(this.coefficient).iterator();

         Double var5;
         while(var3.hasNext()) {
            ComponentCount var4;
            var5 = (var4 = (ComponentCount)var3.next()).getComponent().getSuspiciousnessValue(this.coefficient);
            if (var2.containsKey(var5)) {
               ((List)var2.get(var5)).add(var4.getComponent());
            } else {
               ArrayList var6;
               (var6 = new ArrayList()).add(var4.getComponent());
               var2.put(var5, var6);
            }
         }

         int var15 = 0;
         Iterator var16 = var2.keySet().iterator();

         label66:
         while(var16.hasNext()) {
            var5 = (Double)var16.next();
            Logger.getInstance().debug("Suspiciousness: " + var5);
            List var18 = (List)var2.get(var5);
            var15 += var18.size();
            boolean var17 = false;
            Iterator var19 = var18.iterator();

            while(true) {
               while(var19.hasNext()) {
                  Component var7 = (Component)var19.next();
                  String var8 = var7.getClassLabel() + "#" + var7.getLineNumberLabel();
                  String[] var9;
                  int var10 = (var9 = Properties.FAULTY_COMPONENTS.split(":")).length;

                  for(int var11 = 0; var11 < var10; ++var11) {
                     String var12 = var9[var11];
                     if (var8.equals(var12)) {
                        Logger.getInstance().debug("Suspiciousness of the first faulty component " + var7.getSuspiciousnessValue(this.coefficient));
                        Logger.getInstance().debug("Position of the first faulty component " + var15);
                        var17 = true;
                        break;
                     }
                  }
               }

               if (!var17) {
                  break;
               }
               break label66;
            }
         }

         if (var15 == 0) {
            Logger.getInstance().warn("There is no faulty component(s) described as " + Properties.FAULTY_COMPONENTS);
         }

         double var13 = (double)var15 / (double)var1.getNumberOfComponents();

         assert var13 >= 0.0D;

         assert var13 < (double)var1.getNumberOfComponents();

         return var13;
      }
   }
}

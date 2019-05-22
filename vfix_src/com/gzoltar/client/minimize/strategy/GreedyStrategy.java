package com.gzoltar.client.minimize.strategy;

import com.gzoltar.instrumentation.components.ComponentCount;
import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.instrumentation.testing.TestResult;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GreedyStrategy {
   public Set<String> perform(Spectra var1) {
      HashSet var2 = new HashSet();
      HashSet var3 = new HashSet();
      Iterator var4 = var1.getComponents().iterator();

      while(true) {
         while(true) {
            ComponentCount var5;
            do {
               if (!var4.hasNext()) {
                  int var9 = var3.size();
                  var3.clear();
                  int var10 = 0;

                  while(var10 < var9) {
                     int var12 = 0;
                     TestResult var7 = null;
                     Iterator var11 = var1.getTestResults().iterator();

                     while(var11.hasNext()) {
                        TestResult var8 = (TestResult)var11.next();
                        if (!var2.contains(var8.getName()) && var12 < var8.getCoveredComponents().size()) {
                           var12 = var8.getCoveredComponents().size();
                           var7 = var8;
                        }
                     }

                     var11 = var1.getComponents().iterator();

                     while(var11.hasNext()) {
                        ComponentCount var13 = (ComponentCount)var11.next();
                        if (var7.covers(var13)) {
                           var3.add(var13.getComponent().getLabel());
                        }
                     }

                     var10 = var3.size();
                     var2.add(var7.getName());
                  }

                  assert var10 == var9;

                  assert var10 == var3.size();

                  return var2;
               }

               var5 = (ComponentCount)var4.next();
            } while(var3.contains(var5.getComponent().getLabel()));

            Iterator var6 = var1.getTestResults().iterator();

            while(var6.hasNext()) {
               if (((TestResult)var6.next()).covers(var5)) {
                  var3.add(var5.getComponent().getLabel());
                  break;
               }
            }
         }
      }
   }
}

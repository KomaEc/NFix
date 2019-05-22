package com.gzoltar.instrumentation.spectra;

import com.gzoltar.instrumentation.components.ComponentCount;
import com.gzoltar.instrumentation.testing.TestResult;
import java.util.HashMap;
import java.util.Iterator;

public class FilterComponentsTriggeredByFailingTestCases implements SpectraFilter {
   public void filter(Spectra var1) {
      HashMap var2 = new HashMap();
      Iterator var3 = var1.getTestResults().iterator();

      while(true) {
         TestResult var4;
         Iterator var5;
         String var6;
         ComponentCount var7;
         do {
            if (!var3.hasNext()) {
               if (var2.isEmpty()) {
                  return;
               }

               var3 = var1.getTestResults().iterator();

               while(var3.hasNext()) {
                  var5 = (var4 = (TestResult)var3.next()).getCoveredComponents().iterator();

                  while(var5.hasNext()) {
                     var6 = (String)var5.next();
                     var7 = Spectra.getInstance().getComponentCount(var6);
                     if (!var2.containsKey(var7.getComponent().getLabel())) {
                        var4.removeCoveredComponent(var7);
                     }
                  }
               }

               var1.setComponents(var2);
               return;
            }
         } while((var4 = (TestResult)var3.next()).wasSuccessful());

         var5 = var4.getCoveredComponents().iterator();

         while(var5.hasNext()) {
            var6 = (String)var5.next();
            var7 = Spectra.getInstance().getComponentCount(var6);
            var2.put(var7.getComponent().getLabel(), var7);
         }
      }
   }
}

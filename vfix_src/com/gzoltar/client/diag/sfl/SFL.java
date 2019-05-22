package com.gzoltar.client.diag.sfl;

import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.ComponentCount;
import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.instrumentation.testing.TestResult;
import java.util.Iterator;

public interface SFL {
   default void sfl(Spectra var1) {
      Iterator var2 = var1.getComponents().iterator();

      while(var2.hasNext()) {
         ComponentCount var3 = (ComponentCount)var2.next();
         double var4 = 0.0D;
         double var6 = 0.0D;
         double var8 = 0.0D;
         double var10 = 0.0D;
         Iterator var12 = var1.getTestResults().iterator();

         while(var12.hasNext()) {
            TestResult var13;
            boolean var14 = (var13 = (TestResult)var12.next()).wasSuccessful();
            if (var13.covers(var3)) {
               if (!var14) {
                  ++var4;
               } else {
                  ++var6;
               }
            } else if (!var14) {
               ++var8;
            } else {
               ++var10;
            }
         }

         var3.getComponent().setSuspiciousnessValue(this.getClass().getSimpleName(), this.compute(var10, var8, var6, var4));
         Logger.getInstance().debug(var3.getComponent().getLabel());
         Logger.getInstance().debug("n00: " + var10 + ", n01: " + var8 + ", n10: " + var6 + ", n11: " + var4);
      }

   }

   double compute(double var1, double var3, double var5, double var7);
}

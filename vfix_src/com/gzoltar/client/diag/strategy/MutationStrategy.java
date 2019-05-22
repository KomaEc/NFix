package com.gzoltar.client.diag.strategy;

import com.gzoltar.client.Properties;
import com.gzoltar.client.diag.mutation.Metallaxis;
import com.gzoltar.client.diag.mutation.Muse;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.instrumentation.testing.TestResult;
import com.gzoltar.instrumentation.utils.SystemProperties;
import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

public class MutationStrategy {
   public void diagnose(Spectra var1) {
      switch(Properties.MUTATION_STRATEGY) {
      case MUSE:
         Logger.getInstance().info("* Running MUSE strategy");
         (new Muse()).suspiciouness(var1);
         return;
      default:
         Properties.Coefficient[] var2;
         int var3 = (var2 = Properties.COEFFICIENTS).length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Properties.Coefficient var5 = var2[var4];
            Logger.getInstance().info("* Running METALLAXIS strategy with " + var5.name());
            (new Metallaxis(var5)).suspiciouness(var1);
         }

      }
   }

   public void printMatrix(Map<String, TestResult> var1, Map<String, TestResult> var2, String var3, String var4) {
      try {
         (new File(var3)).mkdirs();
         PrintWriter var9 = new PrintWriter(var3 + SystemProperties.FILE_SEPARATOR + var4, "UTF-8");
         Iterator var10 = var1.keySet().iterator();

         while(var10.hasNext()) {
            String var5 = (String)var10.next();
            StringBuilder var6 = new StringBuilder();
            TestResult var7 = (TestResult)var1.get(var5);

            assert var7 != null;

            TestResult var11 = (TestResult)var2.get(var5);

            assert var11 != null;

            if (var7.wasSuccessful() && var11.wasSuccessful()) {
               var6.append("0 ");
            } else if (var7.wasSuccessful() && !var11.wasSuccessful()) {
               var6.append("1 ");
            } else if (!var7.wasSuccessful() && var11.wasSuccessful()) {
               var6.append("1 ");
            } else if (!var7.wasSuccessful() && !var11.wasSuccessful()) {
               if (!var7.getTrace().equals(var11.getTrace())) {
                  var6.append("1 ");
               } else {
                  var6.append("0 ");
               }
            }

            var6.append(var11.wasSuccessful() ? "+" : "-");
            var9.println(var6);
         }

         var9.close();
      } catch (Exception var8) {
         Logger.getInstance().err("", var8);
      }
   }
}

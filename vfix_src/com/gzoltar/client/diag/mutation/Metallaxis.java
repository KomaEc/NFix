package com.gzoltar.client.diag.mutation;

import com.gzoltar.client.Properties;
import com.gzoltar.client.diag.strategy.CoverageStrategy;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.ComponentCount;
import com.gzoltar.instrumentation.components.Mutant;
import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.instrumentation.testing.TestResult;
import com.gzoltar.instrumentation.utils.SystemProperties;
import java.util.Iterator;

public class Metallaxis implements Mutation {
   private final Properties.Coefficient coefficient;

   public Metallaxis(Properties.Coefficient var1) {
      this.coefficient = var1;
   }

   public void suspiciouness(Spectra var1) {
      Iterator var2 = var1.getComponents().iterator();

      while(true) {
         while(var2.hasNext()) {
            ComponentCount var3;
            if (!(var3 = (ComponentCount)var2.next()).getComponent().hasMutants()) {
               var3.getComponent().setSuspiciousnessValue(this.coefficient.name(), 0.0D);
               Logger.getInstance().debug("component " + var3.getComponent().getLabel() + " without mutants");
            } else {
               double var4 = 0.0D;

               double var19;
               for(Iterator var6 = var3.getComponent().getMutants().iterator(); var6.hasNext(); var4 = Math.max(var4, var19)) {
                  Mutant var7 = (Mutant)var6.next();
                  double var8 = 0.0D;
                  double var10 = 0.0D;
                  double var12 = 0.0D;
                  double var14 = 0.0D;
                  Iterator var16 = var7.getTestResults().keySet().iterator();

                  while(true) {
                     while(var16.hasNext()) {
                        String var17 = (String)var16.next();
                        TestResult var18 = var1.getTestResult(var17);

                        assert var18 != null;

                        TestResult var21 = var7.getTestResult(var17);

                        assert var21 != null;

                        if (var18.wasSuccessful() && var21.wasSuccessful()) {
                           ++var8;
                        } else if (var18.wasSuccessful() && !var21.wasSuccessful()) {
                           ++var12;
                        } else if (!var18.wasSuccessful() && var21.wasSuccessful()) {
                           ++var14;
                        } else if (!var18.wasSuccessful() && !var21.wasSuccessful()) {
                           if (!var18.getTrace().equals(var21.getTrace())) {
                              ++var14;
                           } else {
                              ++var10;
                           }
                        }
                     }

                     var19 = (new CoverageStrategy()).diagnose(this.coefficient, var8, var10, var12, var14);

                     assert var19 != Double.NaN;

                     Logger.getInstance().debug("n00: " + var8 + ", n01: " + var10 + ", n10: " + var12 + ", n11: " + var14 + " -> " + var19);
                     if (Properties.PRINT_SPECTRA) {
                        var1.getComponent(var3.getComponent().getLabel()).setSuspiciousnessValue(this.coefficient.name(), var19);
                        var1.printComponents(Properties.GZOLTAR_DATA_DIR, Properties.MUTANTS_DIR + SystemProperties.FILE_SEPARATOR + Properties.SPECTRA_FILE + "_" + var7.getFullClassName() + "_" + var7.getId(), Properties.VERBOSE_SPECTRA, Properties.INCLUDE_SUSPICIOUSNESS_VALUE);
                     }
                     break;
                  }
               }

               var3.getComponent().setSuspiciousnessValue(this.coefficient.name(), var4);
               Logger.getInstance().debug(var3.getComponent().toString(true, true));
            }
         }

         return;
      }
   }
}

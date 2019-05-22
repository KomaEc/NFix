package com.gzoltar.client.diag.mutation;

import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.ComponentCount;
import com.gzoltar.instrumentation.components.Mutant;
import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.instrumentation.testing.TestResult;
import java.util.Iterator;

public class Muse implements Mutation {
   private double Fp = 0.0D;
   private double Pp = 0.0D;

   public void suspiciouness(Spectra var1) {
      double var2 = this.alpha(var1);
      Iterator var4 = var1.getComponents().iterator();

      while(true) {
         while(var4.hasNext()) {
            ComponentCount var5;
            if (!(var5 = (ComponentCount)var4.next()).getComponent().hasMutants()) {
               var5.getComponent().setSuspiciousnessValue(this.getClass().getSimpleName(), 0.0D);
               Logger.getInstance().debug(var5.getComponent().toString());
            } else {
               double var6 = (double)var5.getComponent().getMutants().size();
               double var8 = 0.0D;

               double var12;
               double var14;
               label65:
               for(Iterator var10 = var5.getComponent().getMutants().iterator(); var10.hasNext(); var8 += var12 / this.Fp - var2 * var14 / this.Pp) {
                  Mutant var11 = (Mutant)var10.next();
                  var12 = 0.0D;
                  var14 = 0.0D;
                  Iterator var16 = var11.getTestResults().keySet().iterator();

                  while(true) {
                     while(true) {
                        TestResult var18;
                        TestResult var21;
                        do {
                           String var17;
                           do {
                              if (!var16.hasNext()) {
                                 continue label65;
                              }

                              var17 = (String)var16.next();
                              var18 = var1.getTestResult(var17);

                              assert var18 != null;
                           } while(!var18.covers(var5));

                           var21 = var11.getTestResult(var17);

                           assert var21 != null;
                        } while(var18.wasSuccessful() == var21.wasSuccessful());

                        if (var18.wasSuccessful() && !var21.wasSuccessful()) {
                           ++var14;
                        } else if (!var18.wasSuccessful() && var21.wasSuccessful()) {
                           ++var12;
                        }
                     }
                  }
               }

               double var19 = 1.0D / var6 * var8;
               var5.getComponent().setSuspiciousnessValue(this.getClass().getSimpleName(), var19);
               Logger.getInstance().debug(var5.getComponent().toString());
            }
         }

         return;
      }
   }

   private double alpha(Spectra var1) {
      double var2 = 0.0D;
      double var4 = 0.0D;
      double var6 = (double)(Integer)var1.getComponents().parallelStream().map((var0) -> {
         return var0.getComponent().getMutants() == null ? 0 : var0.getComponent().getMutants().size();
      }).reduce(0, Integer::sum);
      Iterator var8 = var1.getTestNames().iterator();

      while(var8.hasNext()) {
         String var9 = (String)var8.next();
         TestResult var10 = var1.getTestResult(var9);

         assert var10 != null;

         if (var10.wasSuccessful()) {
            ++this.Pp;
         } else {
            ++this.Fp;
         }

         Iterator var11 = var1.getComponents().iterator();

         label75:
         while(var11.hasNext()) {
            Iterator var13 = ((ComponentCount)var11.next()).getComponent().getMutants().iterator();

            while(true) {
               while(true) {
                  TestResult var12;
                  do {
                     do {
                        if (!var13.hasNext()) {
                           continue label75;
                        }
                     } while((var12 = ((Mutant)var13.next()).getTestResult(var9)) == null);
                  } while(var10.wasSuccessful() == var12.wasSuccessful());

                  if (var10.wasSuccessful() && !var12.wasSuccessful()) {
                     ++var4;
                  } else if (!var10.wasSuccessful() && var12.wasSuccessful()) {
                     ++var2;
                  }
               }
            }
         }
      }

      double var14 = var6 * this.Fp == 0.0D ? 0.0D : var2 / (var6 * this.Fp);
      double var16 = var4 == 0.0D ? 0.0D : var6 * this.Pp / var4;
      double var18 = var14 * var16;

      assert var18 != Double.NaN;

      Logger.getInstance().debug("f2p: " + var2);
      Logger.getInstance().debug("p2f: " + var4);
      Logger.getInstance().debug("Fp: " + this.Fp);
      Logger.getInstance().debug("Pp: " + this.Pp);
      Logger.getInstance().debug("number_of_mutants: " + var6);
      Logger.getInstance().debug("alpha: " + var18);
      return var18;
   }
}

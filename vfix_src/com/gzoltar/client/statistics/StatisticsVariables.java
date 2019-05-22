package com.gzoltar.client.statistics;

import com.gzoltar.client.Properties;
import com.gzoltar.client.diag.metrics.AmbiguityGroups;
import com.gzoltar.client.diag.metrics.Cd;
import com.gzoltar.client.diag.metrics.Entropy;
import com.gzoltar.client.diag.metrics.Expense;
import com.gzoltar.client.diag.metrics.InformationGain;
import com.gzoltar.client.diag.metrics.Rho;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.spectra.Spectra;
import java.util.Map;

public enum StatisticsVariables {
   PROJECT_ID,
   CONFIGURATION_ID,
   NUMBER_OF_CLASSES,
   NUMBER_OF_TEST_CLASSES,
   NUMBER_OF_UNIT_TEST_CASES,
   NUMBER_OF_COMPONENTS,
   AMBIGUITY_GROUPS,
   CD,
   EXPENSE,
   ENTROPY,
   INFORMATION_GAIN,
   RHO,
   NUMBER_OF_MUTANTS;

   public static void augmentStatisticsData(Spectra var0, Map<String, OutputStatisticsVariable> var1) {
      StatisticsVariables[] var2;
      int var3 = (var2 = Properties.OUTPUT_VARIABLES).length;

      label71:
      for(int var4 = 0; var4 < var3; ++var4) {
         StatisticsVariables var5 = var2[var4];
         if (!var1.containsKey(var5.name())) {
            int var6;
            int var7;
            Properties.Coefficient var8;
            String var9;
            Properties.Coefficient[] var10;
            switch(var5) {
            case PROJECT_ID:
               var1.put(PROJECT_ID.name(), new OutputStatisticsVariable(PROJECT_ID.name(), Properties.PROJECTID == null ? "null" : Properties.PROJECTID));
               break;
            case CONFIGURATION_ID:
               var1.put(CONFIGURATION_ID.name(), new OutputStatisticsVariable(CONFIGURATION_ID.name(), Properties.CONFIGURATIONID == null ? "null" : Properties.CONFIGURATIONID));
               break;
            case AMBIGUITY_GROUPS:
               var1.put(AMBIGUITY_GROUPS.name(), new OutputStatisticsVariable(AMBIGUITY_GROUPS.name(), (new AmbiguityGroups()).getMetricValue(var0)));
               break;
            case CD:
               if (Properties.FAULTY_COMPONENTS == null) {
                  break;
               }

               var6 = (var10 = Properties.COEFFICIENTS).length;
               var7 = 0;

               while(true) {
                  if (var7 >= var6) {
                     continue label71;
                  }

                  var8 = var10[var7];
                  var9 = CD.name() + "_" + var8.name();
                  if (!var1.containsKey(var9)) {
                     var1.put(var9, new OutputStatisticsVariable(CD.name(), (new Cd(var8.name())).getMetricValue(var0)));
                  }

                  ++var7;
               }
            case EXPENSE:
               if (Properties.FAULTY_COMPONENTS == null) {
                  break;
               }

               var6 = (var10 = Properties.COEFFICIENTS).length;
               var7 = 0;

               while(true) {
                  if (var7 >= var6) {
                     continue label71;
                  }

                  var8 = var10[var7];
                  var9 = EXPENSE.name() + "_" + var8.name();
                  if (!var1.containsKey(var9)) {
                     var1.put(var9, new OutputStatisticsVariable(EXPENSE.name(), (new Expense(var8.name())).getMetricValue(var0)));
                  }

                  ++var7;
               }
            case ENTROPY:
               var6 = (var10 = Properties.COEFFICIENTS).length;
               var7 = 0;

               while(true) {
                  if (var7 >= var6) {
                     continue label71;
                  }

                  var8 = var10[var7];
                  var1.put(ENTROPY.name() + "_" + var8.name(), new OutputStatisticsVariable(ENTROPY.name(), (new Entropy(var8.name())).getMetricValue(var0)));
                  ++var7;
               }
            case INFORMATION_GAIN:
               var1.put(INFORMATION_GAIN.name(), new OutputStatisticsVariable(INFORMATION_GAIN.name(), (new InformationGain()).getMetricValue(var0)));
               break;
            case RHO:
               var1.put(RHO.name(), new OutputStatisticsVariable(RHO.name(), (new Rho()).getMetricValue(var0)));
               break;
            default:
               Logger.getInstance().err("* Variable '" + var5.name() + "' not supported");
            }
         }
      }

   }
}

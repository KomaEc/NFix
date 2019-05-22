package com.gzoltar.client.diag.strategy;

import com.gzoltar.client.Properties;
import com.gzoltar.client.diag.sfl.Anderberg;
import com.gzoltar.client.diag.sfl.Barinel;
import com.gzoltar.client.diag.sfl.DStar;
import com.gzoltar.client.diag.sfl.Ideal;
import com.gzoltar.client.diag.sfl.Jaccard;
import com.gzoltar.client.diag.sfl.Kulczynski2;
import com.gzoltar.client.diag.sfl.Naish1;
import com.gzoltar.client.diag.sfl.Ochiai;
import com.gzoltar.client.diag.sfl.Ochiai2;
import com.gzoltar.client.diag.sfl.Opt;
import com.gzoltar.client.diag.sfl.Rogers_Tanimoto;
import com.gzoltar.client.diag.sfl.Russel_Rao;
import com.gzoltar.client.diag.sfl.SBI;
import com.gzoltar.client.diag.sfl.Simple_Matching;
import com.gzoltar.client.diag.sfl.Sorensen_Dice;
import com.gzoltar.client.diag.sfl.Tarantula;
import com.gzoltar.client.diag.sr.Barinel_SR;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.ComponentCount;
import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.instrumentation.testing.TestResult;
import com.gzoltar.instrumentation.utils.SystemProperties;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Iterator;

public class CoverageStrategy {
   public void diagnose(Spectra var1) {
      Properties.Coefficient[] var2;
      int var3 = (var2 = Properties.COEFFICIENTS).length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Properties.Coefficient var5 = var2[var4];
         switch(var5) {
         case OCHIAI:
            Logger.getInstance().info("* Running Ochiai");
            (new Ochiai()).sfl(var1);
            break;
         case OCHIAI2:
            Logger.getInstance().info("* Running Ochiai2");
            (new Ochiai2()).sfl(var1);
            break;
         case TARANTULA:
            Logger.getInstance().info("* Running Tarantula");
            (new Tarantula()).sfl(var1);
            break;
         case JACCARD:
            Logger.getInstance().info("* Running Jaccard");
            (new Jaccard()).sfl(var1);
            break;
         case SBI:
            Logger.getInstance().info("* Running SBI");
            (new SBI()).sfl(var1);
            break;
         case KULCZYNSKI2:
            Logger.getInstance().info("* Running Kulczynski2");
            (new Kulczynski2()).sfl(var1);
            break;
         case SORENSEN_DICE:
            Logger.getInstance().info("* Running Sorensen-Dice");
            (new Sorensen_Dice()).sfl(var1);
            break;
         case ANDERBERG:
            Logger.getInstance().info("* Running Anderberg");
            (new Anderberg()).sfl(var1);
            break;
         case SIMPLE_MATCHING:
            Logger.getInstance().info("* Running Simple-matching");
            (new Simple_Matching()).sfl(var1);
            break;
         case ROGERS_TANIMOTO:
            Logger.getInstance().info("* Running Rogers and Tanimoto");
            (new Rogers_Tanimoto()).sfl(var1);
            break;
         case RUSSEL_RAO:
            Logger.getInstance().info("* Running Russel and Rao");
            (new Russel_Rao()).sfl(var1);
            break;
         case DSTAR:
            Logger.getInstance().info("* Running Dstar");
            (new DStar()).sfl(var1);
            break;
         case OPT:
            Logger.getInstance().info("* Running Opt");
            (new Opt()).sfl(var1);
            break;
         case BARINEL:
            Logger.getInstance().info("* Running Barinel");
            (new Barinel()).sfl(var1);
            break;
         case BARINEL_SR:
            Logger.getInstance().info("* Running Barinel (Spectrum-reasoning)");
            (new Barinel_SR()).sr(var1);
            break;
         case IDEAL:
            Logger.getInstance().info("* Running Ideal");
            (new Ideal()).sfl(var1);
            break;
         case NAISH1:
            Logger.getInstance().info("* Running Naish1");
            (new Naish1()).sfl(var1);
         }
      }

   }

   public double diagnose(Properties.Coefficient var1, double var2, double var4, double var6, double var8) {
      switch(var1) {
      case OCHIAI:
         Logger.getInstance().info("* Running Ochiai");
         return (new Ochiai()).compute(var2, var4, var6, var8);
      case OCHIAI2:
         Logger.getInstance().info("* Running Ochiai2");
         return (new Ochiai2()).compute(var2, var4, var6, var8);
      case TARANTULA:
         Logger.getInstance().info("* Running Tarantula");
         return (new Tarantula()).compute(var2, var4, var6, var8);
      case JACCARD:
         Logger.getInstance().info("* Running Jaccard");
         return (new Jaccard()).compute(var2, var4, var6, var8);
      case SBI:
         Logger.getInstance().info("* Running SBI");
         return (new SBI()).compute(var2, var4, var6, var8);
      case KULCZYNSKI2:
         Logger.getInstance().info("* Running Kulczynski2");
         return (new Kulczynski2()).compute(var2, var4, var6, var8);
      case SORENSEN_DICE:
         Logger.getInstance().info("* Running Sorensen-Dice");
         return (new Sorensen_Dice()).compute(var2, var4, var6, var8);
      case ANDERBERG:
         Logger.getInstance().info("* Running Anderberg");
         return (new Anderberg()).compute(var2, var4, var6, var8);
      case SIMPLE_MATCHING:
         Logger.getInstance().info("* Running Simple-matching");
         return (new Simple_Matching()).compute(var2, var4, var6, var8);
      case ROGERS_TANIMOTO:
         Logger.getInstance().info("* Running Rogers and Tanimoto");
         return (new Rogers_Tanimoto()).compute(var2, var4, var6, var8);
      case RUSSEL_RAO:
         Logger.getInstance().info("* Running Russel and Rao");
         return (new Russel_Rao()).compute(var2, var4, var6, var8);
      case DSTAR:
         Logger.getInstance().info("* Running Dstar");
         return (new DStar()).compute(var2, var4, var6, var8);
      case OPT:
         Logger.getInstance().info("* Running Opt");
         return (new Opt()).compute(var2, var4, var6, var8);
      case BARINEL:
         Logger.getInstance().info("* Running Barinel");
         return (new Barinel()).compute(var2, var4, var6, var8);
      case BARINEL_SR:
      default:
         return Double.NaN;
      case IDEAL:
         Logger.getInstance().info("* Running Ideal");
         return (new Ideal()).compute(var2, var4, var6, var8);
      case NAISH1:
         Logger.getInstance().info("* Running Naish1");
         return (new Naish1()).compute(var2, var4, var6, var8);
      }
   }

   public static void printMatrix(Spectra var0, String var1, String var2) {
      try {
         (new File(var1)).mkdirs();
         PrintWriter var9 = new PrintWriter(var1 + SystemProperties.FILE_SEPARATOR + var2, "UTF-8");
         if (Properties.EXTRA_MATRIX_FILE != null && (new File(Properties.EXTRA_MATRIX_FILE)).exists()) {
            BufferedReader var10 = new BufferedReader(new FileReader(Properties.EXTRA_MATRIX_FILE));

            String var3;
            while((var3 = var10.readLine()) != null) {
               var9.println(var3);
            }

            var10.close();
         }

         Iterator var11 = var0.getTestResults().iterator();

         while(true) {
            StringBuilder var4;
            boolean var5;
            do {
               do {
                  if (!var11.hasNext()) {
                     var9.close();
                     return;
                  }

                  TestResult var12 = (TestResult)var11.next();
                  var4 = new StringBuilder();
                  var5 = var12.wasSuccessful();
                  Iterator var6 = var0.getComponentsSortedByLineNumber().iterator();

                  while(var6.hasNext()) {
                     ComponentCount var7 = (ComponentCount)var6.next();
                     if (var12.covers(var7)) {
                        var4.append("1 ");
                     } else {
                        var4.append("0 ");
                     }
                  }
               } while(var4.length() == 0);
            } while(Properties.FILTER_SPECTRA == Properties.FilterSpectra.TEST_CASES_WITHOUT_ANY_COVERAGE && var4.indexOf("1") == -1);

            var4.append(var5 ? "+" : "-");
            var9.println(var4);
         }
      } catch (Exception var8) {
         Logger.getInstance().err("", var8);
      }
   }
}

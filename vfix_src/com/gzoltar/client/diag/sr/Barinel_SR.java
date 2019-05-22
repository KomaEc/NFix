package com.gzoltar.client.diag.sr;

import com.gzoltar.client.Properties;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.instrumentation.utils.SystemProperties;
import com.gzoltar.nnative.BarinelExe;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Barinel_SR implements CandidateRanking, SR {
   public Map<Integer, Double> getRanking(Spectra var1) {
      if (System.getProperty("os.name").toLowerCase().contains("windows")) {
         Logger.getInstance().err("Windows is not supported yet");
         return null;
      } else {
         try {
            BarinelExe var2 = new BarinelExe();
            ArrayList var3;
            (var3 = new ArrayList()).add(var2.getExecPath());
            var3.add(String.valueOf(var1.getNumberOfComponents()));
            var3.add(Properties.GZOLTAR_DATA_DIR + SystemProperties.FILE_SEPARATOR + Properties.MATRIX_FILE);
            var3.add(Staccato.outputFile);
            var3.add(var2.getOutputFile());
            Logger.getInstance().debug(var3.toString());
            ProcessBuilder var8;
            (var8 = new ProcessBuilder(var3)).redirectErrorStream(true);
            final Process var9;
            InputStream var11 = (var9 = var8.start()).getInputStream();
            BufferedInputStream var12 = new BufferedInputStream(var11);
            byte[] var4 = new byte[1024];
            Timer var5 = new Timer();
            if (Properties.TIMELIMIT != -1) {
               var5.schedule(new TimerTask(this) {
                  public final void run() {
                     var9.destroy();
                     Logger.getInstance().err("Process terminated - timeout");
                  }
               }, (long)(Properties.TIMELIMIT * 1000));
            }

            Logger.getInstance().debug(">>> Begin subprocess output");

            int var6;
            while((var6 = var12.read(var4)) != -1) {
               Logger.getInstance().debug(var4, 0, var6);
            }

            Logger.getInstance().debug("<<< End subprocess output");
            int var10 = var9.waitFor();
            var5.cancel();
            if (var10 == 0) {
               Logger.getInstance().debug("Barinel executed with SUCCESS");
               return this.parseOutput(var2.getOutputFile());
            }
         } catch (InterruptedException | IOException var7) {
            Logger.getInstance().err("", var7);
         }

         return null;
      }
   }

   private Map<Integer, Double> parseOutput(String var1) {
      HashMap var2 = new HashMap();
      BufferedReader var3 = null;
      boolean var24 = false;

      label199: {
         try {
            var24 = true;
            var3 = new BufferedReader(new FileReader(var1));

            String var4;
            while((var4 = var3.readLine()) != null) {
               Logger.getInstance().debug(var4);
               String[] var5 = var4.split("  ");

               assert var5.length == 2;

               Double var30 = Double.valueOf(var5[1]);
               if (var5[0].contains("{")) {
                  String[] var7;
                  int var8 = (var7 = var5[0].replace("{", "").replace("}", "").replace(" ", "").split(",")).length;

                  for(int var9 = 0; var9 < var8; ++var9) {
                     Integer var11 = Integer.valueOf(var7[var9]);
                     Object var10000 = var2.containsKey(var11) ? (List)var2.get(var11) : new ArrayList();
                     Object var12 = var10000;
                     ((List)var10000).add(var30);
                     var2.put(var11, var12);
                  }
               } else {
                  ArrayList var6;
                  (var6 = new ArrayList()).add(var30);
                  var2.put(Integer.valueOf(var5[0]), var6);
               }
            }

            var24 = false;
            break label199;
         } catch (IOException var28) {
            Logger.getInstance().err("", var28);
            var24 = false;
         } finally {
            if (var24) {
               try {
                  if (var3 != null) {
                     var3.close();
                  }
               } catch (IOException var27) {
                  Logger.getInstance().err("", var27);
                  return null;
               }

            }
         }

         try {
            if (var3 != null) {
               var3.close();
            }

            return null;
         } catch (IOException var25) {
            Logger.getInstance().err("", var25);
            return null;
         }
      }

      try {
         var3.close();
      } catch (IOException var26) {
         Logger.getInstance().err("", var26);
         return null;
      }

      double var13 = 0.0D;
      HashMap var31 = new HashMap();
      Iterator var32 = var2.keySet().iterator();

      double var15;
      Integer var33;
      while(var32.hasNext()) {
         var33 = (Integer)var32.next();
         Logger.getInstance().debug("Component: " + var33);
         var15 = 0.0D;

         double var17;
         for(Iterator var10 = ((List)var2.get(var33)).iterator(); var10.hasNext(); var15 += var17) {
            var17 = (Double)var10.next();
            Logger.getInstance().debug("  S: " + var17);
         }

         Logger.getInstance().debug("  Score: " + var15);
         var15 /= (double)((List)var2.get(var33)).size();
         Logger.getInstance().debug("  AVG: " + var15);
         var13 += var15;
         var31.put(var33, var15);
      }

      var32 = var31.keySet().iterator();

      while(var32.hasNext()) {
         var33 = (Integer)var32.next();
         Logger.getInstance().debug("Component: " + var33);
         var15 = this.normalize(var13, (Double)var31.get(var33));
         Logger.getInstance().debug("  normalized_score: " + var15);
         var31.put(var33, var15);
      }

      return var31;
   }
}

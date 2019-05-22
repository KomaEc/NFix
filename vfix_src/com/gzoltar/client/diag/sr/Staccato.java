package com.gzoltar.client.diag.sr;

import com.gzoltar.client.Properties;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.instrumentation.utils.SystemProperties;
import com.gzoltar.nnative.StaccatoExe;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Staccato implements CandidateGeneration {
   public static String outputFile = null;

   public List<String> getFaultyComponents(Spectra var1) {
      if (System.getProperty("os.name").toLowerCase().contains("windows")) {
         Logger.getInstance().err("Windows is not supported yet");
         return null;
      } else {
         try {
            StaccatoExe var2 = new StaccatoExe();
            ArrayList var3;
            (var3 = new ArrayList()).add(var2.getExecPath());
            var3.add(String.valueOf(var1.getNumberOfComponents()));
            var3.add(Properties.GZOLTAR_DATA_DIR + SystemProperties.FILE_SEPARATOR + Properties.MATRIX_FILE);
            var3.add(var2.getOutputFile());
            outputFile = var2.getOutputFile();
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
               Logger.getInstance().debug("Staccato executed with SUCCESS");
               return this.parseOutput(var2.getOutputFile());
            }
         } catch (InterruptedException | IOException var7) {
            Logger.getInstance().err("", var7);
         }

         return null;
      }
   }

   private List<String> parseOutput(String var1) {
      ArrayList var2 = new ArrayList();
      BufferedReader var3 = null;
      boolean var9 = false;

      label94: {
         try {
            var9 = true;
            var3 = new BufferedReader(new FileReader(var1));

            while((var1 = var3.readLine()) != null) {
               Logger.getInstance().debug(var1);
               var2.add(var1.replace("h", ""));
            }

            var9 = false;
            break label94;
         } catch (IOException var13) {
            Logger.getInstance().err("", var13);
            var9 = false;
         } finally {
            if (var9) {
               try {
                  if (var3 != null) {
                     var3.close();
                  }
               } catch (IOException var12) {
                  Logger.getInstance().err("", var12);
                  return null;
               }

            }
         }

         try {
            if (var3 != null) {
               var3.close();
            }

            return null;
         } catch (IOException var10) {
            Logger.getInstance().err("", var10);
            return null;
         }
      }

      try {
         var3.close();
         return var2;
      } catch (IOException var11) {
         Logger.getInstance().err("", var11);
         return null;
      }
   }
}

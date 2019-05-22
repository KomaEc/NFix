package com.gzoltar.master.statistics;

import com.gzoltar.client.Properties;
import com.gzoltar.client.statistics.OutputStatisticsVariable;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.utils.SystemProperties;
import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

public class CSVStatisticsBackend extends StatisticsBackend {
   public void writeData(Map<String, OutputStatisticsVariable> var1) {
      Logger.getInstance().info("* Writing statistics to '" + Properties.GZOLTAR_DATA_DIR + SystemProperties.FILE_SEPARATOR + Properties.STATISTICS_FILE + "'");

      try {
         (new File(Properties.GZOLTAR_DATA_DIR)).mkdirs();
         File var2;
         if ((var2 = new File(Properties.GZOLTAR_DATA_DIR + SystemProperties.FILE_SEPARATOR + Properties.STATISTICS_FILE)).exists()) {
            var2.delete();
         }

         PrintWriter var7 = new PrintWriter(var2, "UTF-8");
         Iterator var3 = var1.keySet().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var7.print(var4);
            if (var3.hasNext()) {
               var7.print(",");
            }
         }

         var7.println();
         Iterator var8 = var1.values().iterator();

         while(var8.hasNext()) {
            OutputStatisticsVariable var6 = (OutputStatisticsVariable)var8.next();
            var7.print(var6.getValue());
            if (var8.hasNext()) {
               var7.print(",");
            }
         }

         var7.close();
      } catch (Exception var5) {
         Logger.getInstance().err("", var5);
      }
   }
}

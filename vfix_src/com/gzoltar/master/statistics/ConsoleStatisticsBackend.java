package com.gzoltar.master.statistics;

import com.gzoltar.client.statistics.OutputStatisticsVariable;
import com.gzoltar.instrumentation.Logger;
import java.util.Iterator;
import java.util.Map;

public class ConsoleStatisticsBackend extends StatisticsBackend {
   public void writeData(Map<String, OutputStatisticsVariable> var1) {
      Logger.getInstance().info("* Writing statistics");
      Iterator var3 = var1.values().iterator();

      while(var3.hasNext()) {
         OutputStatisticsVariable var2 = (OutputStatisticsVariable)var3.next();
         Logger.getInstance().info(var2.toString());
      }

   }
}

package com.gzoltar.master.statistics;

import com.gzoltar.client.statistics.OutputStatisticsVariable;
import java.util.Map;

public abstract class StatisticsBackend {
   private static StatisticsBackend instance = null;

   protected StatisticsBackend() {
   }

   public static StatisticsBackend getInstance() {
      // $FF: Couldn't be decompiled
   }

   public abstract void writeData(Map<String, OutputStatisticsVariable> var1);
}

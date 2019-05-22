package com.gzoltar.master.statistics;

import com.gzoltar.client.Properties;

// $FF: synthetic class
final class a {
   // $FF: synthetic field
   static final int[] a = new int[Properties.StatisticsBackend.values().length];

   static {
      try {
         a[Properties.StatisticsBackend.NONE.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         a[Properties.StatisticsBackend.CONSOLE.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }

      try {
         a[Properties.StatisticsBackend.CSV.ordinal()] = 3;
      } catch (NoSuchFieldError var0) {
      }
   }
}

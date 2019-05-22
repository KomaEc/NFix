package com.google.inject.internal.util;

import java.util.logging.Logger;

public final class Stopwatch {
   private static final Logger logger = Logger.getLogger(Stopwatch.class.getName());
   private long start = System.currentTimeMillis();

   public long reset() {
      long now = System.currentTimeMillis();

      long var3;
      try {
         var3 = now - this.start;
      } finally {
         this.start = now;
      }

      return var3;
   }

   public void resetAndLog(String label) {
      Logger var10000 = logger;
      String var2 = String.valueOf(String.valueOf(label));
      long var3 = this.reset();
      var10000.fine((new StringBuilder(24 + var2.length())).append(var2).append(": ").append(var3).append("ms").toString());
   }
}

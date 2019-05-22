package org.netbeans.lib.cvsclient.admin;

public class DateComparator {
   private static final long SECONDS_PER_HOUR = 3600L;
   private static final DateComparator singleton = new DateComparator();

   public static DateComparator getInstance() {
      return singleton;
   }

   private DateComparator() {
   }

   public boolean equals(long var1, long var3) {
      long var5 = var1 / 1000L;
      long var7 = var3 / 1000L;
      long var9 = Math.abs(var5 - var7);
      if (var9 < 1L) {
         return true;
      } else {
         return var9 >= 3599L && var9 <= 3601L;
      }
   }
}

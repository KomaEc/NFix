package com.gzoltar.shaded.org.pitest.util;

public class TimeSpan {
   private long start;
   private long end;

   public TimeSpan(long start, long end) {
      this.start = start;
      this.end = end;
   }

   public long duration() {
      return this.end - this.start;
   }

   public long getStart() {
      return this.start;
   }

   public long getEnd() {
      return this.end;
   }

   public void setStart(long start) {
      this.start = start;
   }

   public void setEnd(long end) {
      this.end = end;
   }

   public String toString() {
      long millis = this.duration();
      int seconds = (int)(millis / 1000L) % 60;
      int minutes = (int)(millis / 60000L % 60L);
      int hours = (int)(millis / 3600000L % 24L);
      if (hours != 0) {
         return "" + hours + " hours, " + minutes + " minutes and " + seconds + " seconds";
      } else if (minutes != 0) {
         return "" + minutes + " minutes and " + seconds + " seconds";
      } else {
         return seconds != 0 ? "" + seconds + " seconds" : "< 1 second";
      }
   }
}

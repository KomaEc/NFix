package com.gzoltar.shaded.org.pitest.mutationtest.statistics;

import com.gzoltar.shaded.org.pitest.mutationtest.DetectionStatus;

public class StatusCount {
   private final DetectionStatus status;
   private long count;

   StatusCount(DetectionStatus status, long count) {
      this.status = status;
      this.count = count;
   }

   void increment() {
      ++this.count;
   }

   public String toString() {
      return "" + this.status + " " + this.count;
   }

   public long getCount() {
      return this.count;
   }

   public DetectionStatus getStatus() {
      return this.status;
   }
}

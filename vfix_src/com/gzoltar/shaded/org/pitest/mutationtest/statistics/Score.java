package com.gzoltar.shaded.org.pitest.mutationtest.statistics;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.F2;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.mutationtest.DetectionStatus;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Score {
   private final String mutatorName;
   private final Map<DetectionStatus, StatusCount> counts;

   public Score(String name) {
      this.mutatorName = name;
      this.counts = createMap();
   }

   private static Map<DetectionStatus, StatusCount> createMap() {
      Map<DetectionStatus, StatusCount> map = new LinkedHashMap();
      DetectionStatus[] arr$ = DetectionStatus.values();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         DetectionStatus each = arr$[i$];
         map.put(each, new StatusCount(each, 0L));
      }

      return map;
   }

   public void registerResult(DetectionStatus result) {
      StatusCount total = (StatusCount)this.counts.get(result);
      total.increment();
   }

   public Iterable<StatusCount> getCounts() {
      return this.counts.values();
   }

   public long getTotalMutations() {
      return (Long)FCollection.fold(this.addTotals(), 0L, this.counts.values());
   }

   public long getTotalDetectedMutations() {
      return (Long)FCollection.fold(this.addTotals(), 0L, FCollection.filter(this.counts.values(), isDetected()));
   }

   public long getPercentageDetected() {
      if (this.getTotalMutations() == 0L) {
         return 100L;
      } else {
         return this.getTotalDetectedMutations() == 0L ? 0L : (long)Math.round(100.0F / (float)this.getTotalMutations() * (float)this.getTotalDetectedMutations());
      }
   }

   private static F<StatusCount, Boolean> isDetected() {
      return new F<StatusCount, Boolean>() {
         public Boolean apply(StatusCount a) {
            return a.getStatus().isDetected();
         }
      };
   }

   private F2<Long, StatusCount, Long> addTotals() {
      return new F2<Long, StatusCount, Long>() {
         public Long apply(Long a, StatusCount b) {
            return a + b.getCount();
         }
      };
   }

   public void report(PrintStream out) {
      out.println("> " + this.mutatorName);
      out.println(">> Generated " + this.getTotalMutations() + " Killed " + this.getTotalDetectedMutations() + " (" + this.getPercentageDetected() + "%)");
      int i = 0;
      StringBuilder sb = new StringBuilder();
      Iterator i$ = this.counts.values().iterator();

      while(i$.hasNext()) {
         StatusCount each = (StatusCount)i$.next();
         sb.append(each + " ");
         ++i;
         if (i % 4 == 0) {
            out.println("> " + sb.toString());
            sb = new StringBuilder();
         }
      }

      out.println("> " + sb.toString());
   }

   public String getMutatorName() {
      return this.mutatorName;
   }
}

package com.gzoltar.shaded.org.pitest.mutationtest.statistics;

import com.gzoltar.shaded.org.pitest.functional.F2;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResult;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MutationStatistics {
   private final Map<String, Score> mutatorTotalMap = new HashMap();
   private long numberOfTestsRun = 0L;

   public void registerResults(Collection<MutationResult> results) {
      FCollection.forEach(results, this.register());
   }

   private SideEffect1<MutationResult> register() {
      return new SideEffect1<MutationResult>() {
         public void apply(MutationResult mr) {
            MutationStatistics.this.numberOfTestsRun = MutationStatistics.this.numberOfTestsRun + (long)mr.getNumberOfTestsRun();
            String key = mr.getDetails().getId().getMutator();
            Score total = (Score)MutationStatistics.this.mutatorTotalMap.get(key);
            if (total == null) {
               total = new Score(key);
               MutationStatistics.this.mutatorTotalMap.put(key, total);
            }

            total.registerResult(mr.getStatus());
         }
      };
   }

   public Iterable<Score> getScores() {
      return this.mutatorTotalMap.values();
   }

   public long getTotalMutations() {
      return (Long)FCollection.fold(addTotals(), 0L, this.mutatorTotalMap.values());
   }

   public long getTotalDetectedMutations() {
      return (Long)FCollection.fold(addDetectedTotals(), 0L, this.mutatorTotalMap.values());
   }

   public long getPercentageDetected() {
      if (this.getTotalMutations() == 0L) {
         return 100L;
      } else {
         return this.getTotalDetectedMutations() == 0L ? 0L : (long)Math.round(100.0F / (float)this.getTotalMutations() * (float)this.getTotalDetectedMutations());
      }
   }

   private static F2<Long, Score, Long> addDetectedTotals() {
      return new F2<Long, Score, Long>() {
         public Long apply(Long a, Score b) {
            return a + b.getTotalDetectedMutations();
         }
      };
   }

   private static F2<Long, Score, Long> addTotals() {
      return new F2<Long, Score, Long>() {
         public Long apply(Long a, Score b) {
            return a + b.getTotalMutations();
         }
      };
   }

   public void report(PrintStream out) {
      out.println(">> Generated " + this.getTotalMutations() + " mutations Killed " + this.getTotalDetectedMutations() + " (" + this.getPercentageDetected() + "%)");
      out.println(">> Ran " + this.numberOfTestsRun + " tests (" + this.getTestsPerMutation() + " tests per mutation)");
   }

   private String getTestsPerMutation() {
      if (this.getTotalMutations() == 0L) {
         return "0";
      } else {
         float testsPerMutation = (float)this.numberOfTestsRun / (float)this.getTotalMutations();
         return (new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH))).format((double)testsPerMutation);
      }
   }
}

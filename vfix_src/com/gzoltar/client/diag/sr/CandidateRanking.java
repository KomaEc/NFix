package com.gzoltar.client.diag.sr;

public interface CandidateRanking {
   default double normalize(double var1, double var3, double var5) {
      return (var5 - var1) / (var3 - var1);
   }

   default double normalize(double var1, double var3) {
      return var3 / var1;
   }
}

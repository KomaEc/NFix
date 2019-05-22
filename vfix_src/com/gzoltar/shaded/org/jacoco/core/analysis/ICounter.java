package com.gzoltar.shaded.org.jacoco.core.analysis;

public interface ICounter {
   int EMPTY = 0;
   int NOT_COVERED = 1;
   int FULLY_COVERED = 2;
   int PARTLY_COVERED = 3;

   double getValue(ICounter.CounterValue var1);

   int getTotalCount();

   int getCoveredCount();

   int getMissedCount();

   double getCoveredRatio();

   double getMissedRatio();

   int getStatus();

   public static enum CounterValue {
      TOTALCOUNT,
      MISSEDCOUNT,
      COVEREDCOUNT,
      MISSEDRATIO,
      COVEREDRATIO;
   }
}

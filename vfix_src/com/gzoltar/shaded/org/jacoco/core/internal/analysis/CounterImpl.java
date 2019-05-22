package com.gzoltar.shaded.org.jacoco.core.internal.analysis;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICounter;

public abstract class CounterImpl implements ICounter {
   private static final int SINGLETON_LIMIT = 30;
   private static final CounterImpl[][] SINGLETONS = new CounterImpl[31][];
   public static final CounterImpl COUNTER_0_0;
   public static final CounterImpl COUNTER_1_0;
   public static final CounterImpl COUNTER_0_1;
   protected int missed;
   protected int covered;

   public static CounterImpl getInstance(int missed, int covered) {
      return (CounterImpl)(missed <= 30 && covered <= 30 ? SINGLETONS[missed][covered] : new CounterImpl.Var(missed, covered));
   }

   public static CounterImpl getInstance(ICounter counter) {
      return getInstance(counter.getMissedCount(), counter.getCoveredCount());
   }

   protected CounterImpl(int missed, int covered) {
      this.missed = missed;
      this.covered = covered;
   }

   public CounterImpl increment(ICounter counter) {
      return this.increment(counter.getMissedCount(), counter.getCoveredCount());
   }

   public abstract CounterImpl increment(int var1, int var2);

   public double getValue(ICounter.CounterValue value) {
      switch(value) {
      case TOTALCOUNT:
         return (double)this.getTotalCount();
      case MISSEDCOUNT:
         return (double)this.getMissedCount();
      case COVEREDCOUNT:
         return (double)this.getCoveredCount();
      case MISSEDRATIO:
         return this.getMissedRatio();
      case COVEREDRATIO:
         return this.getCoveredRatio();
      default:
         throw new AssertionError(value);
      }
   }

   public int getTotalCount() {
      return this.missed + this.covered;
   }

   public int getCoveredCount() {
      return this.covered;
   }

   public int getMissedCount() {
      return this.missed;
   }

   public double getCoveredRatio() {
      return (double)this.covered / (double)(this.missed + this.covered);
   }

   public double getMissedRatio() {
      return (double)this.missed / (double)(this.missed + this.covered);
   }

   public int getStatus() {
      int status = this.covered > 0 ? 2 : 0;
      if (this.missed > 0) {
         status |= 1;
      }

      return status;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ICounter)) {
         return false;
      } else {
         ICounter that = (ICounter)obj;
         return this.missed == that.getMissedCount() && this.covered == that.getCoveredCount();
      }
   }

   public int hashCode() {
      return this.missed ^ this.covered * 17;
   }

   public String toString() {
      StringBuilder b = new StringBuilder("Counter[");
      b.append(this.getMissedCount());
      b.append('/').append(this.getCoveredCount());
      b.append(']');
      return b.toString();
   }

   static {
      for(int i = 0; i <= 30; ++i) {
         SINGLETONS[i] = new CounterImpl[31];

         for(int j = 0; j <= 30; ++j) {
            SINGLETONS[i][j] = new CounterImpl.Fix(i, j);
         }
      }

      COUNTER_0_0 = SINGLETONS[0][0];
      COUNTER_1_0 = SINGLETONS[1][0];
      COUNTER_0_1 = SINGLETONS[0][1];
   }

   private static class Fix extends CounterImpl {
      public Fix(int missed, int covered) {
         super(missed, covered);
      }

      public CounterImpl increment(int missed, int covered) {
         return getInstance(this.missed + missed, this.covered + covered);
      }
   }

   private static class Var extends CounterImpl {
      public Var(int missed, int covered) {
         super(missed, covered);
      }

      public CounterImpl increment(int missed, int covered) {
         this.missed += missed;
         this.covered += covered;
         return this;
      }
   }
}

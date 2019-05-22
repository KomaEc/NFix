package com.gzoltar.shaded.org.pitest.mutationtest.build;

import com.gzoltar.shaded.org.pitest.mutationtest.TimeoutLengthStrategy;

public class PercentAndConstantTimeoutStrategy implements TimeoutLengthStrategy {
   public static final float DEFAULT_FACTOR = 1.25F;
   public static final long DEFAULT_CONSTANT = 4000L;
   private final float percent;
   private final long constant;

   public PercentAndConstantTimeoutStrategy(float percent, long constant) {
      this.percent = percent;
      this.constant = constant;
   }

   public long getAllowedTime(long normalDuration) {
      return (long)Math.round((float)normalDuration * this.percent) + this.constant;
   }
}

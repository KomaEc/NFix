package com.google.common.util.concurrent;

import com.google.common.base.Stopwatch;
import java.util.concurrent.TimeUnit;

final class RateLimiter$SleepingStopwatch$1 extends RateLimiter$SleepingStopwatch {
   final Stopwatch stopwatch = Stopwatch.createStarted();

   long readMicros() {
      return this.stopwatch.elapsed(TimeUnit.MICROSECONDS);
   }

   void sleepMicrosUninterruptibly(long micros) {
      if (micros > 0L) {
         Uninterruptibles.sleepUninterruptibly(micros, TimeUnit.MICROSECONDS);
      }

   }
}

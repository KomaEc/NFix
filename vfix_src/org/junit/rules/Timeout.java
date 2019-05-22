package org.junit.rules;

import java.util.concurrent.TimeUnit;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class Timeout implements TestRule {
   private final long timeout;
   private final TimeUnit timeUnit;
   private final boolean lookForStuckThread;

   public static Timeout.Builder builder() {
      return new Timeout.Builder();
   }

   /** @deprecated */
   @Deprecated
   public Timeout(int millis) {
      this((long)millis, TimeUnit.MILLISECONDS);
   }

   public Timeout(long timeout, TimeUnit timeUnit) {
      this.timeout = timeout;
      this.timeUnit = timeUnit;
      this.lookForStuckThread = false;
   }

   protected Timeout(Timeout.Builder builder) {
      this.timeout = builder.getTimeout();
      this.timeUnit = builder.getTimeUnit();
      this.lookForStuckThread = builder.getLookingForStuckThread();
   }

   public static Timeout millis(long millis) {
      return new Timeout(millis, TimeUnit.MILLISECONDS);
   }

   public static Timeout seconds(long seconds) {
      return new Timeout(seconds, TimeUnit.SECONDS);
   }

   protected final long getTimeout(TimeUnit unit) {
      return unit.convert(this.timeout, this.timeUnit);
   }

   protected final boolean getLookingForStuckThread() {
      return this.lookForStuckThread;
   }

   protected Statement createFailOnTimeoutStatement(Statement statement) throws Exception {
      return FailOnTimeout.builder().withTimeout(this.timeout, this.timeUnit).withLookingForStuckThread(this.lookForStuckThread).build(statement);
   }

   public Statement apply(Statement base, Description description) {
      try {
         return this.createFailOnTimeoutStatement(base);
      } catch (final Exception var4) {
         return new Statement() {
            public void evaluate() throws Throwable {
               throw new RuntimeException("Invalid parameters for Timeout", var4);
            }
         };
      }
   }

   public static class Builder {
      private boolean lookForStuckThread = false;
      private long timeout = 0L;
      private TimeUnit timeUnit;

      protected Builder() {
         this.timeUnit = TimeUnit.SECONDS;
      }

      public Timeout.Builder withTimeout(long timeout, TimeUnit unit) {
         this.timeout = timeout;
         this.timeUnit = unit;
         return this;
      }

      protected long getTimeout() {
         return this.timeout;
      }

      protected TimeUnit getTimeUnit() {
         return this.timeUnit;
      }

      public Timeout.Builder withLookingForStuckThread(boolean enable) {
         this.lookForStuckThread = enable;
         return this;
      }

      protected boolean getLookingForStuckThread() {
         return this.lookForStuckThread;
      }

      public Timeout build() {
         return new Timeout(this);
      }
   }
}

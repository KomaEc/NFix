package org.apache.velocity.runtime.log;

/** @deprecated */
public class PrimordialLogSystem extends HoldingLogChute implements LogSystem {
   /** @deprecated */
   public void logVelocityMessage(int level, String message) {
      this.log(level, message);
   }

   /** @deprecated */
   public void dumpLogMessages(LogSystem newLogger) {
      this.transferTo(new LogChuteSystem(newLogger));
   }
}

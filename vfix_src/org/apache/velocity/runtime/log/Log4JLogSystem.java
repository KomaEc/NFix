package org.apache.velocity.runtime.log;

/** @deprecated */
public class Log4JLogSystem extends Log4JLogChute implements LogSystem {
   /** @deprecated */
   public void logVelocityMessage(int level, String message) {
      this.log(level, message);
   }
}

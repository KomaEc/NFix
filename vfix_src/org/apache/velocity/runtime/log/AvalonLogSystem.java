package org.apache.velocity.runtime.log;

public class AvalonLogSystem extends AvalonLogChute implements LogSystem {
   /** @deprecated */
   public void logVelocityMessage(int level, String message) {
      this.log(level, message);
   }
}

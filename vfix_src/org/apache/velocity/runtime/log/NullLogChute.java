package org.apache.velocity.runtime.log;

import org.apache.velocity.runtime.RuntimeServices;

public class NullLogChute implements LogChute {
   public void init(RuntimeServices rs) throws Exception {
   }

   public void log(int level, String message) {
   }

   public void log(int level, String message, Throwable t) {
   }

   public boolean isLevelEnabled(int level) {
      return false;
   }
}

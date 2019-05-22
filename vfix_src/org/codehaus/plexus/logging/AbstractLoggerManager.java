package org.codehaus.plexus.logging;

public abstract class AbstractLoggerManager implements LoggerManager {
   public void setThreshold(String role, int threshold) {
      this.setThreshold(role, (String)null, threshold);
   }

   public int getThreshold(String role) {
      return this.getThreshold(role, (String)null);
   }

   public Logger getLoggerForComponent(String role) {
      return this.getLoggerForComponent(role, (String)null);
   }

   public void returnComponentLogger(String role) {
      this.returnComponentLogger(role, (String)null);
   }

   protected String toMapKey(String role, String roleHint) {
      return roleHint == null ? role : role + ":" + roleHint;
   }

   // $FF: synthetic method
   public abstract int getActiveLoggerCount();

   // $FF: synthetic method
   public abstract void returnComponentLogger(String var1, String var2);

   // $FF: synthetic method
   public abstract Logger getLoggerForComponent(String var1, String var2);

   // $FF: synthetic method
   public abstract int getThreshold(String var1, String var2);

   // $FF: synthetic method
   public abstract void setThreshold(String var1, String var2, int var3);

   // $FF: synthetic method
   public abstract int getThreshold();

   // $FF: synthetic method
   public abstract void setThreshold(int var1);
}

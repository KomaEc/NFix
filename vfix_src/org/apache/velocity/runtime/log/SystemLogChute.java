package org.apache.velocity.runtime.log;

import org.apache.velocity.runtime.RuntimeServices;

public class SystemLogChute implements LogChute {
   public static final String RUNTIME_LOG_LEVEL_KEY = "runtime.log.logsystem.system.level";
   public static final String RUNTIME_LOG_SYSTEM_ERR_LEVEL_KEY = "runtime.log.logsystem.system.err.level";
   private int enabled = -1;
   private int errLevel = -1;

   public void init(RuntimeServices rs) throws Exception {
      String level = (String)rs.getProperty("runtime.log.logsystem.system.level");
      if (level != null) {
         this.setEnabledLevel(this.toLevel(level));
      }

      String errLevel = (String)rs.getProperty("runtime.log.logsystem.system.err.level");
      if (errLevel != null) {
         this.setSystemErrLevel(this.toLevel(errLevel));
      }

   }

   protected int toLevel(String level) {
      if (level.equalsIgnoreCase("debug")) {
         return 0;
      } else if (level.equalsIgnoreCase("info")) {
         return 1;
      } else if (level.equalsIgnoreCase("warn")) {
         return 2;
      } else {
         return level.equalsIgnoreCase("error") ? 3 : -1;
      }
   }

   protected String getPrefix(int level) {
      switch(level) {
      case -1:
         return " [trace] ";
      case 0:
         return " [debug] ";
      case 1:
         return "  [info] ";
      case 2:
         return "  [warn] ";
      case 3:
         return " [error] ";
      default:
         return "  [info] ";
      }
   }

   public void log(int level, String message) {
      this.log(level, message, (Throwable)null);
   }

   public void log(int level, String message, Throwable t) {
      if (this.isLevelEnabled(level)) {
         String prefix = this.getPrefix(level);
         if (level >= this.errLevel) {
            System.err.print(prefix);
            System.err.println(message);
            if (t != null) {
               System.err.println(t.getMessage());
               t.printStackTrace();
            }
         } else {
            System.out.print(prefix);
            System.out.println(message);
            if (t != null) {
               System.out.println(t.getMessage());
               t.printStackTrace(System.out);
            }
         }

      }
   }

   public void setEnabledLevel(int level) {
      this.enabled = level;
   }

   public int getEnabledLevel() {
      return this.enabled;
   }

   public void setSystemErrLevel(int level) {
      this.errLevel = level;
   }

   public int getSystemErrLevel() {
      return this.errLevel;
   }

   public boolean isLevelEnabled(int level) {
      return level >= this.enabled;
   }
}

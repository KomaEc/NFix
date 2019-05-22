package org.apache.velocity.runtime.log;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log.Hierarchy;
import org.apache.log.LogTarget;
import org.apache.log.Logger;
import org.apache.log.Priority;
import org.apache.log.output.io.FileTarget;
import org.apache.velocity.runtime.RuntimeServices;

public class AvalonLogChute implements LogChute {
   public static final String AVALON_LOGGER = "runtime.log.logsystem.avalon.logger";
   public static final String AVALON_LOGGER_FORMAT = "runtime.log.logsystem.avalon.format";
   public static final String AVALON_LOGGER_LEVEL = "runtime.log.logsystem.avalon.level";
   private Logger logger = null;
   private RuntimeServices rsvc = null;
   private static final Map logLevels = new HashMap();

   public void init(RuntimeServices rs) throws Exception {
      this.rsvc = rs;
      String name = (String)this.rsvc.getProperty("runtime.log.logsystem.avalon.logger");
      if (name != null) {
         this.logger = Hierarchy.getDefaultHierarchy().getLoggerFor(name);
      } else {
         this.logger = Hierarchy.getDefaultHierarchy().getLoggerFor(this.rsvc.toString());
         String file = (String)this.rsvc.getProperty("runtime.log");
         if (StringUtils.isNotEmpty(file)) {
            this.initTarget(file, this.rsvc);
         }
      }

   }

   private void initTarget(String file, RuntimeServices rsvc) throws Exception {
      try {
         String format = null;
         Priority level = null;
         if (rsvc != null) {
            format = rsvc.getString("runtime.log.logsystem.avalon.format", "%{time} %{message}\\n%{throwable}");
            level = (Priority)logLevels.get(rsvc.getString("runtime.log.logsystem.avalon.level", "debug"));
         }

         VelocityFormatter vf = new VelocityFormatter(format);
         FileTarget target = new FileTarget(new File(file), false, vf);
         this.logger.setPriority(level);
         this.logger.setLogTargets(new LogTarget[]{target});
         this.log(0, "AvalonLogChute initialized using file '" + file + '\'');
      } catch (IOException var7) {
         rsvc.getLog().warn("Unable to create log file for AvalonLogChute", var7);
         throw new Exception("Error configuring AvalonLogChute : " + var7);
      }
   }

   /** @deprecated */
   public void init(String file) throws Exception {
      this.logger = Hierarchy.getDefaultHierarchy().getLoggerFor(this.rsvc.toString());
      this.initTarget(file, (RuntimeServices)null);
      this.log(2, "You shouldn't be using the init(String file) method!");
   }

   public void log(int level, String message) {
      switch(level) {
      case -1:
         this.logger.debug(" [trace] " + message);
         break;
      case 0:
         this.logger.debug(" [debug] " + message);
         break;
      case 1:
         this.logger.info("  [info] " + message);
         break;
      case 2:
         this.logger.warn("  [warn] " + message);
         break;
      case 3:
         this.logger.error(" [error] " + message);
         break;
      default:
         this.logger.info(message);
      }

   }

   public void log(int level, String message, Throwable t) {
      switch(level) {
      case -1:
         this.logger.debug(" [trace] " + message, t);
         break;
      case 0:
         this.logger.debug(" [debug] " + message, t);
         break;
      case 1:
         this.logger.info("  [info] " + message, t);
         break;
      case 2:
         this.logger.warn("  [warn] " + message, t);
         break;
      case 3:
         this.logger.error(" [error] " + message, t);
         break;
      default:
         this.logger.info(message, t);
      }

   }

   public boolean isLevelEnabled(int level) {
      switch(level) {
      case -1:
      case 0:
         return this.logger.isDebugEnabled();
      case 1:
         return this.logger.isInfoEnabled();
      case 2:
         return this.logger.isWarnEnabled();
      case 3:
         return this.logger.isErrorEnabled();
      default:
         return true;
      }
   }

   protected void finalize() throws Throwable {
      this.shutdown();
   }

   public void shutdown() {
      this.logger.unsetLogTargets();
   }

   static {
      logLevels.put("trace", Priority.DEBUG);
      logLevels.put("debug", Priority.DEBUG);
      logLevels.put("info", Priority.INFO);
      logLevels.put("warn", Priority.WARN);
      logLevels.put("error", Priority.ERROR);
   }
}

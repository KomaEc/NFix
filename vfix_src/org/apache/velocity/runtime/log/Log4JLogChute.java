package org.apache.velocity.runtime.log;

import java.io.IOException;
import java.lang.reflect.Field;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.util.ExceptionUtils;

public class Log4JLogChute implements LogChute {
   public static final String RUNTIME_LOG_LOG4J_LOGGER = "runtime.log.logsystem.log4j.logger";
   private RuntimeServices rsvc = null;
   private boolean hasTrace = false;
   private RollingFileAppender appender = null;
   protected Logger logger = null;
   // $FF: synthetic field
   static Class class$org$apache$log4j$Level;

   public void init(RuntimeServices rs) throws Exception {
      this.rsvc = rs;
      String name = (String)this.rsvc.getProperty("runtime.log.logsystem.log4j.logger");
      if (name != null) {
         this.logger = Logger.getLogger(name);
         this.log(0, "Log4JLogChute using logger '" + name + '\'');
      } else {
         this.logger = Logger.getLogger(this.getClass().getName());
         String file = this.rsvc.getString("runtime.log");
         if (file != null && file.length() > 0) {
            this.initAppender(file);
         }
      }

      try {
         Field traceLevel = (class$org$apache$log4j$Level == null ? (class$org$apache$log4j$Level = class$("org.apache.log4j.Level")) : class$org$apache$log4j$Level).getField("TRACE");
         this.hasTrace = true;
      } catch (NoSuchFieldException var4) {
         this.log(0, "The version of log4j being used does not support the \"trace\" level.");
      }

   }

   private void initAppender(String file) throws Exception {
      try {
         PatternLayout layout = new PatternLayout("%d - %m%n");
         this.appender = new RollingFileAppender(layout, file, true);
         this.appender.setMaxBackupIndex(1);
         this.appender.setMaximumFileSize(100000L);
         this.logger.setAdditivity(false);
         this.logger.setLevel(Level.DEBUG);
         this.logger.addAppender(this.appender);
         this.log(0, "Log4JLogChute initialized using file '" + file + '\'');
      } catch (IOException var3) {
         this.rsvc.getLog().warn("Could not create file appender '" + file + '\'', var3);
         throw ExceptionUtils.createRuntimeException("Error configuring Log4JLogChute : ", var3);
      }
   }

   public void log(int level, String message) {
      switch(level) {
      case -1:
         if (this.hasTrace) {
            this.logger.trace(message);
         } else {
            this.logger.debug(message);
         }
         break;
      case 0:
         this.logger.debug(message);
         break;
      case 1:
         this.logger.info(message);
         break;
      case 2:
         this.logger.warn(message);
         break;
      case 3:
         this.logger.error(message);
         break;
      default:
         this.logger.debug(message);
      }

   }

   public void log(int level, String message, Throwable t) {
      switch(level) {
      case -1:
         if (this.hasTrace) {
            this.logger.trace(message, t);
         } else {
            this.logger.debug(message, t);
         }
         break;
      case 0:
         this.logger.debug(message, t);
         break;
      case 1:
         this.logger.info(message, t);
         break;
      case 2:
         this.logger.warn(message, t);
         break;
      case 3:
         this.logger.error(message, t);
         break;
      default:
         this.logger.debug(message, t);
      }

   }

   public boolean isLevelEnabled(int level) {
      switch(level) {
      case -1:
         if (this.hasTrace) {
            return this.logger.isTraceEnabled();
         }

         return this.logger.isDebugEnabled();
      case 0:
         return this.logger.isDebugEnabled();
      case 1:
         return this.logger.isInfoEnabled();
      case 2:
         return this.logger.isEnabledFor(Level.WARN);
      case 3:
         return this.logger.isEnabledFor(Level.ERROR);
      default:
         return true;
      }
   }

   protected void finalize() throws Throwable {
      this.shutdown();
   }

   public void shutdown() {
      if (this.appender != null) {
         this.logger.removeAppender(this.appender);
         this.appender.close();
         this.appender = null;
      }

   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}

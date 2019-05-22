package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.context;

import org.slf4j.Logger;
import org.slf4j.Marker;

public enum LogLevel {
   TRACE {
      public void log(Logger logger, String message) {
         logger.trace(message);
      }

      public void log(Logger logger, Marker marker, String message) {
         logger.trace(marker, message);
      }
   },
   DEBUG {
      public void log(Logger logger, String message) {
         logger.debug(message);
      }

      public void log(Logger logger, Marker marker, String message) {
         logger.debug(marker, message);
      }
   },
   INFO {
      public void log(Logger logger, String message) {
         logger.info(message);
      }

      public void log(Logger logger, Marker marker, String message) {
         logger.info(marker, message);
      }
   },
   WARN {
      public void log(Logger logger, String message) {
         logger.warn(message);
      }

      public void log(Logger logger, Marker marker, String message) {
         logger.warn(marker, message);
      }
   },
   ERROR {
      public void log(Logger logger, String message) {
         logger.error(message);
      }

      public void log(Logger logger, Marker marker, String message) {
         logger.error(marker, message);
      }
   };

   private LogLevel() {
   }

   public abstract void log(Logger var1, String var2);

   public abstract void log(Logger var1, Marker var2, String var3);

   // $FF: synthetic method
   LogLevel(LogLevel var3) {
      this();
   }
}

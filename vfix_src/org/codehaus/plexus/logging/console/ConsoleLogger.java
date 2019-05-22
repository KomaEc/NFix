package org.codehaus.plexus.logging.console;

import org.codehaus.plexus.logging.AbstractLogger;
import org.codehaus.plexus.logging.Logger;

public final class ConsoleLogger extends AbstractLogger {
   public ConsoleLogger(int threshold, String name) {
      super(threshold, name);
   }

   public void debug(String message, Throwable throwable) {
      if (this.isDebugEnabled()) {
         System.out.print("[DEBUG] ");
         System.out.println(message);
         if (null != throwable) {
            throwable.printStackTrace(System.out);
         }
      }

   }

   public void info(String message, Throwable throwable) {
      if (this.isInfoEnabled()) {
         System.out.print("[INFO] ");
         System.out.println(message);
         if (null != throwable) {
            throwable.printStackTrace(System.out);
         }
      }

   }

   public void warn(String message, Throwable throwable) {
      if (this.isWarnEnabled()) {
         System.out.print("[WARNING] ");
         System.out.println(message);
         if (null != throwable) {
            throwable.printStackTrace(System.out);
         }
      }

   }

   public void error(String message, Throwable throwable) {
      if (this.isErrorEnabled()) {
         System.out.print("[ERROR] ");
         System.out.println(message);
         if (null != throwable) {
            throwable.printStackTrace(System.out);
         }
      }

   }

   public void fatalError(String message, Throwable throwable) {
      if (this.isFatalErrorEnabled()) {
         System.out.print("[FATAL ERROR] ");
         System.out.println(message);
         if (null != throwable) {
            throwable.printStackTrace(System.out);
         }
      }

   }

   public Logger getChildLogger(String name) {
      return this;
   }
}

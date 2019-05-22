package org.codehaus.plexus.logging;

public abstract class AbstractLogEnabled implements LogEnabled {
   private Logger logger;

   public void enableLogging(Logger logger) {
      this.logger = logger;
   }

   protected Logger getLogger() {
      return this.logger;
   }

   protected void setupLogger(Object component) {
      this.setupLogger(component, this.logger);
   }

   protected void setupLogger(Object component, String subCategory) {
      if (subCategory == null) {
         throw new IllegalStateException("Logging category must be defined.");
      } else {
         Logger logger = this.logger.getChildLogger(subCategory);
         this.setupLogger(component, logger);
      }
   }

   protected void setupLogger(Object component, Logger logger) {
      if (component instanceof LogEnabled) {
         ((LogEnabled)component).enableLogging(logger);
      }

   }
}

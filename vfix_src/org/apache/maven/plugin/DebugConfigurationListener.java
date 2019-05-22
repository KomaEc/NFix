package org.apache.maven.plugin;

import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.logging.Logger;

public class DebugConfigurationListener implements ConfigurationListener {
   private Logger logger;

   public DebugConfigurationListener(Logger logger) {
      this.logger = logger;
   }

   public void notifyFieldChangeUsingSetter(String fieldName, Object value, Object target) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug("  (s) " + fieldName + " = " + value);
      }

   }

   public void notifyFieldChangeUsingReflection(String fieldName, Object value, Object target) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug("  (f) " + fieldName + " = " + value);
      }

   }
}

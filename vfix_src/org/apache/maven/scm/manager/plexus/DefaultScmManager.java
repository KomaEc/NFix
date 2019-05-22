package org.apache.maven.scm.manager.plexus;

import java.util.HashMap;
import java.util.Map;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.manager.AbstractScmManager;
import org.apache.maven.scm.provider.ScmProvider;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;

public class DefaultScmManager extends AbstractScmManager implements Initializable, LogEnabled {
   private Map<String, ScmProvider> scmProviders;
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

   public void initialize() {
      if (this.scmProviders == null) {
         this.scmProviders = new HashMap(0);
      }

      if (this.getLogger().isWarnEnabled() && this.scmProviders.size() == 0) {
         this.getLogger().warn("No SCM providers configured.");
      }

      this.setScmProviders(this.scmProviders);
   }

   protected ScmLogger getScmLogger() {
      return new PlexusLogger(this.getLogger());
   }
}

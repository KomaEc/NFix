package org.apache.maven.scm.provider.jazz.command.consumer;

import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.CommandLineUtils;

public class ErrorConsumer extends CommandLineUtils.StringStreamConsumer {
   private boolean fFed = false;
   private ScmLogger logger;

   public ErrorConsumer(ScmLogger logger) {
      this.logger = logger;
   }

   public boolean hasBeenFed() {
      return this.fFed;
   }

   public void consumeLine(String line) {
      this.logger.debug("ErrorConsumer.consumeLine: " + line);
      this.fFed = true;
      super.consumeLine(line);
   }
}

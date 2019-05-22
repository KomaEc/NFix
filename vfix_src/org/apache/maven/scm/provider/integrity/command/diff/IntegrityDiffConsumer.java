package org.apache.maven.scm.provider.integrity.command.diff;

import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class IntegrityDiffConsumer implements StreamConsumer {
   private ScmLogger logger;

   public IntegrityDiffConsumer(ScmLogger logger) {
      this.logger = logger;
   }

   public void consumeLine(String line) {
      this.logger.info(line);
   }
}

package org.apache.maven.scm.provider.integrity.command.fileinfo;

import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class IntegrityFileInfoConsumer implements StreamConsumer {
   private ScmLogger logger;

   public IntegrityFileInfoConsumer(ScmLogger logger) {
      this.logger = logger;
   }

   public void consumeLine(String line) {
      this.logger.info(line);
   }
}

package org.apache.maven.scm.provider.synergy.consumer;

import java.io.File;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SynergyWorkareaConsumer implements StreamConsumer {
   private ScmLogger logger;
   private File workarea;

   public SynergyWorkareaConsumer(ScmLogger logger) {
      this.logger = logger;
   }

   public void consumeLine(String line) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug(line);
      }

      if (line.indexOf(" '") > -1) {
         int beginIndex = line.indexOf(" '");
         String fileName = line.substring(beginIndex + 2, line.indexOf("'", beginIndex + 2));
         this.workarea = new File(fileName);
      }

   }

   public File getWorkAreaPath() {
      return this.workarea;
   }
}

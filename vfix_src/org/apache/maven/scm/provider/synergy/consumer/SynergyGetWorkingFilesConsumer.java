package org.apache.maven.scm.provider.synergy.consumer;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SynergyGetWorkingFilesConsumer implements StreamConsumer {
   private ScmLogger logger;
   private List<String> files = new ArrayList();
   public static final String OUTPUT_FORMAT = "%name";

   public SynergyGetWorkingFilesConsumer(ScmLogger logger) {
      this.logger = logger;
   }

   public void consumeLine(String line) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug(line);
      }

      if (!line.trim().equals("")) {
         this.files.add(line);
      }

   }

   public List<String> getFiles() {
      return this.files;
   }
}

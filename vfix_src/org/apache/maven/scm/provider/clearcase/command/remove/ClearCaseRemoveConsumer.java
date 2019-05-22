package org.apache.maven.scm.provider.clearcase.command.remove;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class ClearCaseRemoveConsumer implements StreamConsumer {
   private ScmLogger logger;
   private List<ScmFile> removedFiles = new ArrayList();

   public ClearCaseRemoveConsumer(ScmLogger logger) {
      this.logger = logger;
   }

   public void consumeLine(String line) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug(line);
      }

      int beginIndex = line.indexOf(34);
      if (beginIndex != -1) {
         String fileName = line.substring(beginIndex + 1, line.indexOf(34, beginIndex + 1));
         this.removedFiles.add(new ScmFile(fileName, ScmFileStatus.DELETED));
      }

   }

   public List<ScmFile> getRemovedFiles() {
      return this.removedFiles;
   }
}

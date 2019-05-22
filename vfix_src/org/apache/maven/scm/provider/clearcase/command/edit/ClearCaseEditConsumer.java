package org.apache.maven.scm.provider.clearcase.command.edit;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class ClearCaseEditConsumer implements StreamConsumer {
   private ScmLogger logger;
   private List<ScmFile> editFiles = new ArrayList();

   public ClearCaseEditConsumer(ScmLogger logger) {
      this.logger = logger;
   }

   public void consumeLine(String line) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug(line);
      }

      int beginIndex = line.indexOf(34);
      if (beginIndex != -1) {
         String fileName = line.substring(beginIndex + 1, line.indexOf(34, beginIndex + 1));
         this.editFiles.add(new ScmFile(fileName, ScmFileStatus.EDITED));
      }

   }

   public List<ScmFile> getEditFiles() {
      return this.editFiles;
   }
}

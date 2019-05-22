package org.apache.maven.scm.provider.clearcase.command.status;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class ClearCaseStatusConsumer implements StreamConsumer {
   private ScmLogger logger;
   private File workingDirectory;
   private List<ScmFile> checkedOutFiles = new ArrayList();

   public ClearCaseStatusConsumer(ScmLogger logger, File workingDirectory) {
      this.logger = logger;
      this.workingDirectory = workingDirectory;
   }

   public void consumeLine(String line) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug(line);
      }

      this.checkedOutFiles.add(new ScmFile(this.workingDirectory.getAbsolutePath() + line.substring(1), ScmFileStatus.CHECKED_OUT));
   }

   public List<ScmFile> getCheckedOutFiles() {
      return this.checkedOutFiles;
   }
}

package org.apache.maven.scm.provider.clearcase.command.checkout;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class ClearCaseCheckOutConsumer implements StreamConsumer {
   private ScmLogger logger;
   private List<ScmFile> checkedOutFiles = new ArrayList();

   public ClearCaseCheckOutConsumer(ScmLogger logger) {
      this.logger = logger;
   }

   public void consumeLine(String line) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug("line " + line);
      }

      this.checkedOutFiles.add(new ScmFile(line, ScmFileStatus.CHECKED_OUT));
   }

   public List<ScmFile> getCheckedOutFiles() {
      return this.checkedOutFiles;
   }
}

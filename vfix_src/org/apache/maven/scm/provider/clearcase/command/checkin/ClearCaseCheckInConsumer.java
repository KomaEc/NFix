package org.apache.maven.scm.provider.clearcase.command.checkin;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class ClearCaseCheckInConsumer implements StreamConsumer {
   private ScmLogger logger;
   private List<ScmFile> checkedInFiles = new ArrayList();

   public ClearCaseCheckInConsumer(ScmLogger logger) {
      this.logger = logger;
   }

   public void consumeLine(String line) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug(line);
      }

      int beginIndex = line.indexOf(34);
      if (beginIndex != -1) {
         String fileName = line.substring(beginIndex + 1, line.indexOf(34, beginIndex + 1));
         this.checkedInFiles.add(new ScmFile(fileName, ScmFileStatus.CHECKED_IN));
      }

   }

   public List<ScmFile> getCheckedInFiles() {
      return this.checkedInFiles;
   }
}

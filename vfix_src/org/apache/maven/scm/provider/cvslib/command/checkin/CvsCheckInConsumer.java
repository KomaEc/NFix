package org.apache.maven.scm.provider.cvslib.command.checkin;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class CvsCheckInConsumer implements StreamConsumer {
   private List<ScmFile> checkedInFiles = new ArrayList();
   private String remotePath;
   private ScmLogger logger;

   public CvsCheckInConsumer(String remotePath, ScmLogger logger) {
      this.remotePath = remotePath;
      this.logger = logger;
   }

   public void consumeLine(String line) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug(line);
      }

      int end = line.indexOf(",v  <--  ");
      if (end != -1) {
         String fileName = line.substring(0, end);
         if (fileName.startsWith(this.remotePath)) {
            fileName = fileName.substring(this.remotePath.length());
            this.checkedInFiles.add(new ScmFile(fileName, ScmFileStatus.CHECKED_IN));
         }
      }
   }

   public List<ScmFile> getCheckedInFiles() {
      return this.checkedInFiles;
   }
}

package org.apache.maven.scm.provider.svn.svnexe.command.add;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SvnAddConsumer implements StreamConsumer {
   private ScmLogger logger;
   private List<ScmFile> addedFiles = new ArrayList();

   public SvnAddConsumer(ScmLogger logger) {
      this.logger = logger;
   }

   public void consumeLine(String line) {
      if (line.length() <= 3) {
         if (this.logger.isWarnEnabled()) {
            this.logger.warn("Unexpected input, the line must be at least three characters long. Line: '" + line + "'.");
         }

      } else {
         String statusString = line.substring(0, 1);
         String file = line.substring(3);
         if (statusString.equals("A")) {
            ScmFileStatus status = ScmFileStatus.ADDED;
            this.addedFiles.add(new ScmFile(file, status));
         } else {
            if (this.logger.isInfoEnabled()) {
               this.logger.info("Unknown file status: '" + statusString + "'.");
            }

         }
      }
   }

   public List<ScmFile> getAddedFiles() {
      return this.addedFiles;
   }
}

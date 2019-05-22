package org.apache.maven.scm.provider.cvslib.command.status;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class CvsStatusConsumer implements StreamConsumer {
   private ScmLogger logger;
   private File workingDirectory;
   private List<ScmFile> changedFiles = new ArrayList();

   public CvsStatusConsumer(ScmLogger logger, File workingDirectory) {
      this.logger = logger;
      this.workingDirectory = workingDirectory;
   }

   public void consumeLine(String line) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug(line);
      }

      if (line.length() < 3) {
         if (StringUtils.isNotEmpty(line) && this.logger.isWarnEnabled()) {
            this.logger.warn("Unable to parse output from command: line length must be bigger than 3. (" + line + ").");
         }

      } else {
         String statusString = line.substring(0, 1);
         String file = line.substring(2);
         ScmFileStatus status;
         if (statusString.equals("A")) {
            status = ScmFileStatus.ADDED;
         } else if (statusString.equals("M")) {
            status = ScmFileStatus.MODIFIED;
         } else if (statusString.equals("D")) {
            status = ScmFileStatus.DELETED;
         } else if (statusString.equals("C")) {
            status = ScmFileStatus.CONFLICT;
         } else {
            if (!statusString.equals("?")) {
               if (!statusString.equals("U") && !statusString.equals("P")) {
                  if (this.logger.isInfoEnabled()) {
                     this.logger.info("Unknown file status: '" + statusString + "'.");
                  }

                  return;
               }

               return;
            }

            status = ScmFileStatus.UNKNOWN;
         }

         if (status.equals(ScmFileStatus.DELETED) || (new File(this.workingDirectory, file)).isFile()) {
            this.changedFiles.add(new ScmFile(file, status));
         }
      }
   }

   public List<ScmFile> getChangedFiles() {
      return this.changedFiles;
   }
}

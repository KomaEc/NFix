package org.apache.maven.scm.provider.git.gitexe.command.diff;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitDiffRawConsumer implements StreamConsumer {
   private ScmLogger logger;
   private List<ScmFile> changedFiles = new ArrayList();

   public GitDiffRawConsumer(ScmLogger logger) {
      this.logger = logger;
   }

   public void consumeLine(String line) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug(line);
      }

      if (!StringUtils.isEmpty(line)) {
         ScmFileStatus status = null;
         String[] parts = line.split("\\s", 6);
         if (parts.length != 6) {
            this.logger.warn("Skipping line because it doesn't contain the right status parameters: " + line);
         } else {
            String modus = parts[4];
            String file = parts[5];
            if ("A".equals(modus)) {
               status = ScmFileStatus.ADDED;
            } else if ("M".equals(modus)) {
               status = ScmFileStatus.UPDATED;
            } else {
               if (!"D".equals(modus)) {
                  this.logger.warn("unknown status detected in line: " + line);
                  return;
               }

               status = ScmFileStatus.DELETED;
            }

            this.changedFiles.add(new ScmFile(file, status));
         }
      }
   }

   public List<ScmFile> getChangedFiles() {
      return this.changedFiles;
   }
}

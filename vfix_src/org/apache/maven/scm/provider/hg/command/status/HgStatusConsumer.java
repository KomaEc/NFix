package org.apache.maven.scm.provider.hg.command.status;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.hg.command.HgConsumer;

class HgStatusConsumer extends HgConsumer {
   private final List<ScmFile> repositoryStatus = new ArrayList();
   private final File workingDir;

   HgStatusConsumer(ScmLogger logger, File workingDir) {
      super(logger);
      this.workingDir = workingDir;
   }

   public void doConsume(ScmFileStatus status, String trimmedLine) {
      File tmpFile = new File(this.workingDir, trimmedLine);
      if (!tmpFile.exists()) {
         if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info("Not a file: " + tmpFile + ". Ignoring");
         }
      } else if (tmpFile.isDirectory()) {
         if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info("New directory added: " + tmpFile);
         }
      } else {
         ScmFile scmFile = new ScmFile(trimmedLine, status);
         if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info(scmFile.toString());
         }

         this.repositoryStatus.add(scmFile);
      }

   }

   List<ScmFile> getStatus() {
      return this.repositoryStatus;
   }
}

package org.apache.maven.scm.provider.bazaar.command.status;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.bazaar.command.BazaarConsumer;

class BazaarStatusConsumer extends BazaarConsumer {
   private final List<ScmFile> repositoryStatus = new ArrayList();
   private final File workingDir;
   private ScmFileStatus currentState = null;

   BazaarStatusConsumer(ScmLogger logger, File workingDir) {
      super(logger);
      this.workingDir = workingDir;
   }

   public void doConsume(ScmFileStatus status, String trimmedLine) {
      if (status != null) {
         this.currentState = status;
      } else if (this.currentState != null) {
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
            ScmFile scmFile = new ScmFile(trimmedLine, this.currentState);
            if (this.getLogger().isInfoEnabled()) {
               this.getLogger().info(scmFile.toString());
            }

            this.repositoryStatus.add(scmFile);
         }

      }
   }

   List<ScmFile> getStatus() {
      return this.repositoryStatus;
   }
}

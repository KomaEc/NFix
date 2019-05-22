package org.apache.maven.scm.provider.bazaar.command.add;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.bazaar.command.BazaarConsumer;

public class BazaarAddConsumer extends BazaarConsumer {
   private final File workingDir;
   private final List<ScmFile> addedFiles = new ArrayList();

   public BazaarAddConsumer(ScmLogger logger, File workingDir) {
      super(logger);
      this.workingDir = workingDir;
   }

   public void doConsume(ScmFileStatus status, String trimmedLine) {
      if (status != null && status == ScmFileStatus.ADDED) {
         File tmpFile = new File(this.workingDir, trimmedLine);
         if (!tmpFile.exists()) {
            if (this.getLogger().isWarnEnabled()) {
               this.getLogger().warn("Not a file: " + tmpFile + ". Ignored");
            }
         } else {
            ScmFile scmFile = new ScmFile(trimmedLine, ScmFileStatus.ADDED);
            if (this.getLogger().isInfoEnabled()) {
               this.getLogger().info(scmFile.toString());
            }

            this.addedFiles.add(scmFile);
         }
      }

   }

   public List<ScmFile> getAddedFiles() {
      return this.addedFiles;
   }
}

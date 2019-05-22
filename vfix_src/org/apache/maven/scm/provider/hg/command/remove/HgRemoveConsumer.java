package org.apache.maven.scm.provider.hg.command.remove;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.hg.command.HgConsumer;

public class HgRemoveConsumer extends HgConsumer {
   private final File workingDir;
   private final List<ScmFile> removedFiles = new ArrayList();

   public HgRemoveConsumer(ScmLogger logger, File workingDir) {
      super(logger);
      this.workingDir = workingDir;
   }

   public void doConsume(ScmFileStatus status, String trimmedLine) {
      if (status != null && status == ScmFileStatus.DELETED) {
         File tmpFile = new File(this.workingDir, trimmedLine);
         if (!tmpFile.exists()) {
            if (this.getLogger().isWarnEnabled()) {
               this.getLogger().warn("Not a file: " + tmpFile + ". Ignored");
            }
         } else {
            ScmFile scmFile = new ScmFile(trimmedLine, ScmFileStatus.DELETED);
            if (this.getLogger().isInfoEnabled()) {
               this.getLogger().info(scmFile.toString());
            }

            this.removedFiles.add(scmFile);
         }
      }

   }

   public List<ScmFile> getRemovedFiles() {
      return this.removedFiles;
   }
}

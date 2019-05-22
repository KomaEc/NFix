package org.apache.maven.scm.provider.git.gitexe.command.list;

import java.io.File;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.git.gitexe.command.AbstractFileCheckingConsumer;

public class GitListConsumer extends AbstractFileCheckingConsumer {
   private ScmFileStatus fileStatus;

   public GitListConsumer(ScmLogger logger, File workingDirectory, ScmFileStatus fileStatus) {
      super(logger, workingDirectory);
      this.fileStatus = fileStatus;
   }

   protected void parseLine(String line) {
      this.addFile(new ScmFile(line, this.fileStatus));
   }

   public List<ScmFile> getListedFiles() {
      return this.getFiles();
   }
}

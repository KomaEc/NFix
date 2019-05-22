package org.apache.maven.scm.command.remove;

import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmResult;

public class RemoveScmResult extends ScmResult {
   private static final long serialVersionUID = 8852310735079996771L;
   private List<ScmFile> removedFiles;

   public RemoveScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
   }

   public RemoveScmResult(String commandLine, List<ScmFile> removedFiles) {
      super(commandLine, (String)null, (String)null, true);
      this.removedFiles = removedFiles;
   }

   public RemoveScmResult(List<ScmFile> removedFiles, ScmResult result) {
      super(result);
      this.removedFiles = removedFiles;
   }

   public List<ScmFile> getRemovedFiles() {
      return this.removedFiles;
   }
}

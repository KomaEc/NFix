package org.apache.maven.scm.command.branch;

import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmResult;

public class BranchScmResult extends ScmResult {
   private static final long serialVersionUID = -4241972929129557932L;
   private List<ScmFile> branchedFiles;

   public BranchScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
   }

   public BranchScmResult(String commandLine, List<ScmFile> branchedFiles) {
      super(commandLine, (String)null, (String)null, true);
      this.branchedFiles = branchedFiles;
   }

   public BranchScmResult(List<ScmFile> branchedFiles, ScmResult result) {
      super(result);
      this.branchedFiles = branchedFiles;
   }

   public List<ScmFile> getBranchedFiles() {
      return this.branchedFiles;
   }
}

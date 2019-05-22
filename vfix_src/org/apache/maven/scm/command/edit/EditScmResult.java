package org.apache.maven.scm.command.edit;

import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmResult;

public class EditScmResult extends ScmResult {
   private static final long serialVersionUID = -6274938710679161288L;
   private List<ScmFile> editFiles;

   public EditScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
   }

   public EditScmResult(String commandLine, List<ScmFile> editFiles) {
      super(commandLine, (String)null, (String)null, true);
      this.editFiles = editFiles;
   }

   public EditScmResult(List<ScmFile> editFiles, ScmResult result) {
      super(result);
      this.editFiles = editFiles;
   }

   public List<ScmFile> getEditFiles() {
      return this.editFiles;
   }
}

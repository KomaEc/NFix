package org.apache.maven.scm.command.export;

import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmResult;

public class ExportScmResult extends ScmResult {
   private static final long serialVersionUID = 8564643361304165292L;
   private List<ScmFile> exportedFiles;

   public ExportScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
   }

   public ExportScmResult(String commandLine, List<ScmFile> updatedFiles) {
      super(commandLine, (String)null, (String)null, true);
      this.exportedFiles = updatedFiles;
   }

   public List<ScmFile> getExportedFiles() {
      return this.exportedFiles;
   }
}

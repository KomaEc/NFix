package org.apache.maven.scm.command.export;

import java.util.List;
import org.apache.maven.scm.ScmFile;

public class ExportScmResultWithRevision extends ExportScmResult {
   private static final long serialVersionUID = -7962912849216079039L;
   private String revision;

   public ExportScmResultWithRevision(String commandLine, String providerMessage, String commandOutput, String revision, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
      this.revision = revision;
   }

   public ExportScmResultWithRevision(String commandLine, List<ScmFile> exportedFiles, String revision) {
      super(commandLine, exportedFiles);
      this.revision = revision;
   }

   public String getRevision() {
      return this.revision;
   }
}

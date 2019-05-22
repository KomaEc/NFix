package org.apache.maven.scm.command.update;

import java.util.List;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmResult;

public class UpdateScmResultWithRevision extends UpdateScmResult {
   private static final long serialVersionUID = 7644079089026359667L;
   private String revision;

   public UpdateScmResultWithRevision(String commandLine, String providerMessage, String commandOutput, String revision, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
      this.revision = revision;
   }

   public UpdateScmResultWithRevision(String commandLine, List<ScmFile> updatedFiles, String revision) {
      super(commandLine, updatedFiles);
      this.revision = revision;
   }

   public UpdateScmResultWithRevision(List<ScmFile> updatedFiles, List<ChangeSet> changes, String revision, ScmResult result) {
      super(updatedFiles, changes, result);
      this.revision = revision;
   }

   public String getRevision() {
      return this.revision;
   }
}

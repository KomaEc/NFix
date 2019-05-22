package org.apache.maven.scm.command.update;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmResult;

public class UpdateScmResult extends ScmResult {
   private static final long serialVersionUID = 1L;
   private List<ScmFile> updatedFiles;
   private List<ChangeSet> changes;

   public UpdateScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
   }

   public UpdateScmResult(String commandLine, List<ScmFile> updatedFiles) {
      super(commandLine, (String)null, (String)null, true);
      this.updatedFiles = updatedFiles;
   }

   public UpdateScmResult(List<ScmFile> updatedFiles, List<ChangeSet> changes, ScmResult result) {
      super(result);
      this.updatedFiles = updatedFiles;
      this.changes = changes;
   }

   public List<ScmFile> getUpdatedFiles() {
      return this.updatedFiles;
   }

   public List<ChangeSet> getChanges() {
      return (List)(this.changes == null ? new ArrayList() : this.changes);
   }

   public void setChanges(List<ChangeSet> changes) {
      this.changes = changes;
   }
}

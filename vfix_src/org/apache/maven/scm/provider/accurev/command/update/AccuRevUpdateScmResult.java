package org.apache.maven.scm.provider.accurev.command.update;

import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.command.update.UpdateScmResultWithRevision;

public class AccuRevUpdateScmResult extends UpdateScmResultWithRevision {
   private static final long serialVersionUID = -4896981432286000329L;
   String fromRevision;

   public AccuRevUpdateScmResult(String commandLine, String providerMessage, String commandOutput, String fromRevision, String toRevision, boolean success) {
      super(commandLine, providerMessage, commandOutput, toRevision, success);
      this.fromRevision = fromRevision;
   }

   public AccuRevUpdateScmResult(String commandLines, List<ScmFile> updatedFiles, String fromRevision, String toRevision) {
      super(commandLines, updatedFiles, toRevision);
      this.fromRevision = fromRevision;
   }

   public String getFromRevision() {
      return this.fromRevision;
   }

   public String getToRevision() {
      return this.getRevision();
   }
}

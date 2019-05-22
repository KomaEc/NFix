package org.apache.maven.scm.provider.git.command.update;

import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.command.update.UpdateScmResultWithRevision;

/** @deprecated */
public class GitUpdateScmResult extends UpdateScmResultWithRevision {
   private static final long serialVersionUID = 7360578324181996847L;

   public GitUpdateScmResult(String commandLine, List<ScmFile> updatedFiles, int revision) {
      super(commandLine, updatedFiles, String.valueOf(revision));
   }
}

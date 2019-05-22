package org.apache.maven.scm.provider.svn.command.update;

import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.command.update.UpdateScmResultWithRevision;

/** @deprecated */
public class SvnUpdateScmResult extends UpdateScmResultWithRevision {
   private static final long serialVersionUID = -3233977852698721693L;

   public SvnUpdateScmResult(String commandLine, List<ScmFile> updatedFiles, int revision) {
      super(commandLine, updatedFiles, String.valueOf(revision));
   }
}

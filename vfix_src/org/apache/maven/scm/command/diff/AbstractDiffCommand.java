package org.apache.maven.scm.command.diff;

import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;

public abstract class AbstractDiffCommand extends AbstractCommand {
   protected abstract DiffScmResult executeDiffCommand(ScmProviderRepository var1, ScmFileSet var2, ScmVersion var3, ScmVersion var4) throws ScmException;

   public ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      ScmVersion startRevision = parameters.getScmVersion(CommandParameter.START_SCM_VERSION, (ScmVersion)null);
      ScmVersion endRevision = parameters.getScmVersion(CommandParameter.END_SCM_VERSION, (ScmVersion)null);
      return this.executeDiffCommand(repository, fileSet, startRevision, endRevision);
   }
}

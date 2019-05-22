package org.apache.maven.scm.command.status;

import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;

public abstract class AbstractStatusCommand extends AbstractCommand {
   protected abstract StatusScmResult executeStatusCommand(ScmProviderRepository var1, ScmFileSet var2) throws ScmException;

   public ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return this.executeStatusCommand(repository, fileSet);
   }
}

package org.apache.maven.scm.command.remove;

import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;

public abstract class AbstractRemoveCommand extends AbstractCommand {
   protected abstract ScmResult executeRemoveCommand(ScmProviderRepository var1, ScmFileSet var2, String var3) throws ScmException;

   protected ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      String message = parameters.getString(CommandParameter.MESSAGE);
      return this.executeRemoveCommand(repository, fileSet, message);
   }
}

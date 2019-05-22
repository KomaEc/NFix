package org.apache.maven.scm.command.add;

import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;

public abstract class AbstractAddCommand extends AbstractCommand {
   protected abstract ScmResult executeAddCommand(ScmProviderRepository var1, ScmFileSet var2, String var3, boolean var4) throws ScmException;

   protected ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return this.executeAddCommand(repository, fileSet, parameters.getString(CommandParameter.MESSAGE, "no message"), parameters.getBoolean(CommandParameter.BINARY));
   }
}

package org.apache.maven.scm.command.lock;

import java.io.File;
import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;

public abstract class AbstractLockCommand extends AbstractCommand {
   protected abstract ScmResult executeLockCommand(ScmProviderRepository var1, File var2, String var3) throws ScmException;

   public ScmResult executeCommand(ScmProviderRepository repository, File workingDirectory, CommandParameters parameters) throws ScmException {
      String file = parameters.getString(CommandParameter.FILE);
      return this.executeLockCommand(repository, workingDirectory, file);
   }
}

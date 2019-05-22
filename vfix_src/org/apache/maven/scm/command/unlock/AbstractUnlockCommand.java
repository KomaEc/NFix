package org.apache.maven.scm.command.unlock;

import java.io.File;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;

public abstract class AbstractUnlockCommand extends AbstractCommand {
   protected abstract ScmResult executeUnlockCommand(ScmProviderRepository var1, File var2) throws ScmException;

   public ScmResult executeCommand(ScmProviderRepository repository, File workingDirectory, CommandParameters parameters) throws ScmException {
      return this.executeUnlockCommand(repository, workingDirectory);
   }
}

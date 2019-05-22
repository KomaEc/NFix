package org.apache.maven.scm.command.blame;

import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;

public abstract class AbstractBlameCommand extends AbstractCommand {
   public abstract BlameScmResult executeBlameCommand(ScmProviderRepository var1, ScmFileSet var2, String var3) throws ScmException;

   protected ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet workingDirectory, CommandParameters parameters) throws ScmException {
      String file = parameters.getString(CommandParameter.FILE);
      return this.executeBlameCommand(repository, workingDirectory, file);
   }
}

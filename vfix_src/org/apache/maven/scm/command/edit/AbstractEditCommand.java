package org.apache.maven.scm.command.edit;

import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;

public abstract class AbstractEditCommand extends AbstractCommand {
   public ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return this.executeEditCommand(repository, fileSet);
   }

   protected abstract ScmResult executeEditCommand(ScmProviderRepository var1, ScmFileSet var2) throws ScmException;
}

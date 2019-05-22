package org.apache.maven.scm.command.checkout;

import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;

public abstract class AbstractCheckOutCommand extends AbstractCommand {
   protected CheckOutScmResult executeCheckOutCommand(ScmProviderRepository repository, ScmFileSet fileSet, ScmVersion scmVersion) throws ScmException {
      return this.executeCheckOutCommand(repository, fileSet, scmVersion, true);
   }

   protected abstract CheckOutScmResult executeCheckOutCommand(ScmProviderRepository var1, ScmFileSet var2, ScmVersion var3, boolean var4) throws ScmException;

   public ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      ScmVersion scmVersion = parameters.getScmVersion(CommandParameter.SCM_VERSION, (ScmVersion)null);
      String recursiveParam = parameters.getString(CommandParameter.RECURSIVE, (String)null);
      if (recursiveParam != null) {
         boolean recursive = parameters.getBoolean(CommandParameter.RECURSIVE);
         return this.executeCheckOutCommand(repository, fileSet, scmVersion, recursive);
      } else {
         return this.executeCheckOutCommand(repository, fileSet, scmVersion);
      }
   }
}

package org.apache.maven.scm.command.remoteinfo;

import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;

public abstract class AbstractRemoteInfoCommand extends AbstractCommand {
   public abstract RemoteInfoScmResult executeRemoteInfoCommand(ScmProviderRepository var1, ScmFileSet var2, CommandParameters var3) throws ScmException;

   protected ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return this.executeRemoteInfoCommand(repository, fileSet, parameters);
   }
}

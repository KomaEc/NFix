package org.apache.maven.scm.provider.cvslib.command.status;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.status.AbstractStatusCommand;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.cvslib.command.CvsCommand;
import org.apache.maven.scm.provider.cvslib.command.CvsCommandUtils;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;
import org.codehaus.plexus.util.cli.Commandline;

public abstract class AbstractCvsStatusCommand extends AbstractStatusCommand implements CvsCommand {
   protected StatusScmResult executeStatusCommand(ScmProviderRepository repo, ScmFileSet fileSet) throws ScmException {
      CvsScmProviderRepository repository = (CvsScmProviderRepository)repo;
      Commandline cl = CvsCommandUtils.getBaseCommand("update", repository, fileSet, "-n");
      cl.createArg().setValue("-d");
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + cl);
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      return this.executeCvsCommand(cl);
   }

   protected abstract StatusScmResult executeCvsCommand(Commandline var1) throws ScmException;
}

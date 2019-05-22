package org.apache.maven.scm.provider.cvslib.command.branch;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.branch.AbstractBranchCommand;
import org.apache.maven.scm.command.branch.BranchScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.cvslib.command.CvsCommand;
import org.apache.maven.scm.provider.cvslib.command.CvsCommandUtils;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;
import org.codehaus.plexus.util.cli.Commandline;

public abstract class AbstractCvsBranchCommand extends AbstractBranchCommand implements CvsCommand {
   protected ScmResult executeBranchCommand(ScmProviderRepository repo, ScmFileSet fileSet, String branchName, String message) throws ScmException {
      CvsScmProviderRepository repository = (CvsScmProviderRepository)repo;
      Commandline cl = CvsCommandUtils.getBaseCommand("tag", repository, fileSet, false);
      cl.createArg().setValue("-b");
      cl.createArg().setValue("-F");
      cl.createArg().setValue("-c");
      cl.createArg().setValue(branchName);
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + cl);
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      return this.executeCvsCommand(cl);
   }

   protected abstract BranchScmResult executeCvsCommand(Commandline var1) throws ScmException;
}

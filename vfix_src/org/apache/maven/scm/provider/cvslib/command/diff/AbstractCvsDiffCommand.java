package org.apache.maven.scm.provider.cvslib.command.diff;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.diff.AbstractDiffCommand;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.cvslib.command.CvsCommand;
import org.apache.maven.scm.provider.cvslib.command.CvsCommandUtils;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

public abstract class AbstractCvsDiffCommand extends AbstractDiffCommand implements CvsCommand {
   protected DiffScmResult executeDiffCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion startRevision, ScmVersion endRevision) throws ScmException {
      CvsScmProviderRepository repository = (CvsScmProviderRepository)repo;
      Commandline cl = CvsCommandUtils.getBaseCommand("diff", repository, fileSet);
      cl.createArg().setValue("-u");
      if (this.isSupportNewFileParameter()) {
         cl.createArg().setValue("-N");
      }

      if (startRevision != null && StringUtils.isNotEmpty(startRevision.getName())) {
         cl.createArg().setValue("-r");
         cl.createArg().setValue(startRevision.getName());
      }

      if (endRevision != null && StringUtils.isNotEmpty(endRevision.getName())) {
         cl.createArg().setValue("-r");
         cl.createArg().setValue(endRevision.getName());
      }

      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + cl);
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      return this.executeCvsCommand(cl);
   }

   protected abstract DiffScmResult executeCvsCommand(Commandline var1) throws ScmException;

   protected boolean isSupportNewFileParameter() {
      return true;
   }
}

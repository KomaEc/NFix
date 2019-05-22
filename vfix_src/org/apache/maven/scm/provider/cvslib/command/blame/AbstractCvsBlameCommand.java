package org.apache.maven.scm.provider.cvslib.command.blame;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.blame.AbstractBlameCommand;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.cvslib.command.CvsCommand;
import org.apache.maven.scm.provider.cvslib.command.CvsCommandUtils;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;
import org.codehaus.plexus.util.cli.Commandline;

public abstract class AbstractCvsBlameCommand extends AbstractBlameCommand implements CvsCommand {
   public BlameScmResult executeBlameCommand(ScmProviderRepository repo, ScmFileSet fileSet, String filename) throws ScmException {
      CvsScmProviderRepository repository = (CvsScmProviderRepository)repo;
      Commandline cl = CvsCommandUtils.getBaseCommand("annotate", repository, fileSet);
      cl.createArg().setValue(filename);
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + cl);
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      return this.executeCvsCommand(cl, repository);
   }

   protected abstract BlameScmResult executeCvsCommand(Commandline var1, CvsScmProviderRepository var2) throws ScmException;
}

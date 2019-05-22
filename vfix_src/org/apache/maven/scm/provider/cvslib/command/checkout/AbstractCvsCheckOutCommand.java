package org.apache.maven.scm.provider.cvslib.command.checkout;

import java.io.IOException;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkout.AbstractCheckOutCommand;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.cvslib.command.CvsCommand;
import org.apache.maven.scm.provider.cvslib.command.CvsCommandUtils;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

public abstract class AbstractCvsCheckOutCommand extends AbstractCheckOutCommand implements CvsCommand {
   protected CheckOutScmResult executeCheckOutCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion version, boolean recursive) throws ScmException {
      if (fileSet.getBasedir().exists()) {
         try {
            FileUtils.deleteDirectory(fileSet.getBasedir());
         } catch (IOException var7) {
            if (this.getLogger().isWarnEnabled()) {
               this.getLogger().warn("Can't delete " + fileSet.getBasedir().getAbsolutePath(), var7);
            }
         }
      }

      CvsScmProviderRepository repository = (CvsScmProviderRepository)repo;
      Commandline cl = CvsCommandUtils.getBaseCommand("checkout", repository, fileSet);
      cl.setWorkingDirectory(fileSet.getBasedir().getParentFile().getAbsolutePath());
      if (version != null && !StringUtils.isEmpty(version.getName())) {
         cl.createArg().setValue("-r");
         cl.createArg().setValue(version.getName());
      }

      cl.createArg().setValue("-d");
      cl.createArg().setValue(fileSet.getBasedir().getName());
      cl.createArg().setValue(repository.getModule());
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + cl);
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      return this.executeCvsCommand(cl);
   }

   protected abstract CheckOutScmResult executeCvsCommand(Commandline var1) throws ScmException;
}

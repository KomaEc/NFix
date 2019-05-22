package org.apache.maven.scm.provider.jazz.command.checkout;

import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmTag;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkout.AbstractCheckOutCommand;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.jazz.command.JazzScmCommand;
import org.apache.maven.scm.provider.jazz.command.consumer.ErrorConsumer;
import org.apache.maven.scm.provider.jazz.repository.JazzScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;

public class JazzCheckOutCommand extends AbstractCheckOutCommand {
   protected CheckOutScmResult executeCheckOutCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion scmVersion, boolean recursive) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("Executing checkout command...");
      }

      JazzScmProviderRepository jazzRepo = (JazzScmProviderRepository)repo;
      JazzScmCommand checkoutCmd = this.createJazzLoadCommand(jazzRepo, fileSet, scmVersion);
      JazzCheckOutConsumer checkoutConsumer = new JazzCheckOutConsumer(repo, this.getLogger());
      ErrorConsumer errConsumer = new ErrorConsumer(this.getLogger());
      int status = checkoutCmd.execute(checkoutConsumer, errConsumer);
      return status == 0 && !errConsumer.hasBeenFed() ? new CheckOutScmResult(checkoutCmd.getCommandString(), checkoutConsumer.getCheckedOutFiles()) : new CheckOutScmResult(checkoutCmd.getCommandString(), "Error code for Jazz SCM checkout (load) command - " + status, errConsumer.getOutput(), false);
   }

   public JazzScmCommand createJazzLoadCommand(JazzScmProviderRepository repo, ScmFileSet fileSet, ScmVersion scmVersion) {
      JazzScmCommand command = new JazzScmCommand("load", "--force", repo, fileSet, this.getLogger());
      if (fileSet != null) {
         command.addArgument("--dir");
         command.addArgument(fileSet.getBasedir().getAbsolutePath());
      }

      String workspace = repo.getRepositoryWorkspace();
      if (scmVersion != null && StringUtils.isNotEmpty(scmVersion.getName())) {
         if (scmVersion instanceof ScmTag) {
            workspace = scmVersion.getName();
         } else if (scmVersion instanceof ScmBranch) {
            workspace = scmVersion.getName();
         }
      }

      command.addArgument(workspace);
      return command;
   }
}

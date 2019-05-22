package org.apache.maven.scm.provider.bazaar.command.status;

import java.io.File;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.command.status.AbstractStatusCommand;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.bazaar.BazaarUtils;

public class BazaarStatusCommand extends AbstractStatusCommand implements Command {
   public StatusScmResult executeStatusCommand(ScmProviderRepository repo, ScmFileSet fileSet) throws ScmException {
      File workingDir = fileSet.getBasedir();
      BazaarStatusConsumer consumer = new BazaarStatusConsumer(this.getLogger(), workingDir);
      String[] statusCmd = new String[]{"status"};
      ScmResult result = BazaarUtils.execute(consumer, this.getLogger(), workingDir, statusCmd);
      return new StatusScmResult(consumer.getStatus(), result);
   }
}

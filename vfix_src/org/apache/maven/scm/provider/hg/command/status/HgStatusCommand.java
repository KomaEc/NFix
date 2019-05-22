package org.apache.maven.scm.provider.hg.command.status;

import java.io.File;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.command.status.AbstractStatusCommand;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.hg.HgUtils;

public class HgStatusCommand extends AbstractStatusCommand implements Command {
   public StatusScmResult executeStatusCommand(ScmProviderRepository repo, ScmFileSet fileSet) throws ScmException {
      File workingDir = fileSet.getBasedir();
      HgStatusConsumer consumer = new HgStatusConsumer(this.getLogger(), workingDir);
      String[] statusCmd = new String[]{"status"};
      ScmResult result = HgUtils.execute(consumer, this.getLogger(), workingDir, statusCmd);
      return new StatusScmResult(consumer.getStatus(), result);
   }
}

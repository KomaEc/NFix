package org.apache.maven.scm.provider.bazaar.command.blame;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.blame.AbstractBlameCommand;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.bazaar.BazaarUtils;

public class BazaarBlameCommand extends AbstractBlameCommand {
   public static final String BLAME_CMD = "blame";

   public BlameScmResult executeBlameCommand(ScmProviderRepository repo, ScmFileSet workingDirectory, String filename) throws ScmException {
      String[] cmd = new String[]{"blame", "--all", "--long", filename};
      BazaarBlameConsumer consumer = new BazaarBlameConsumer(this.getLogger());
      ScmResult result = BazaarUtils.execute(consumer, this.getLogger(), workingDirectory.getBasedir(), cmd);
      return new BlameScmResult(consumer.getLines(), result);
   }
}

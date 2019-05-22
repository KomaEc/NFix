package org.apache.maven.scm.provider.hg.command.blame;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.blame.AbstractBlameCommand;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.hg.HgUtils;

public class HgBlameCommand extends AbstractBlameCommand {
   public static final String BLAME_CMD = "blame";

   public BlameScmResult executeBlameCommand(ScmProviderRepository repo, ScmFileSet workingDirectory, String filename) throws ScmException {
      String[] cmd = new String[]{"blame", "--user", "--date", "--changeset", filename};
      HgBlameConsumer consumer = new HgBlameConsumer(this.getLogger());
      ScmResult result = HgUtils.execute(consumer, this.getLogger(), workingDirectory.getBasedir(), cmd);
      return new BlameScmResult(consumer.getLines(), result);
   }
}

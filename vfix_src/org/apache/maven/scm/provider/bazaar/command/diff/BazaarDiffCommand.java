package org.apache.maven.scm.provider.bazaar.command.diff;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.command.diff.AbstractDiffCommand;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.bazaar.BazaarUtils;
import org.codehaus.plexus.util.StringUtils;

public class BazaarDiffCommand extends AbstractDiffCommand implements Command {
   protected DiffScmResult executeDiffCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion startRevision, ScmVersion endRevision) throws ScmException {
      String[] diffCmd;
      if (startRevision != null && StringUtils.isNotEmpty(startRevision.getName())) {
         String revArg = startRevision.getName();
         if (endRevision != null && StringUtils.isNotEmpty(endRevision.getName())) {
            revArg = revArg + ".." + endRevision.getName();
         }

         diffCmd = new String[]{"diff", "--revision", revArg};
      } else {
         diffCmd = new String[]{"diff"};
      }

      diffCmd = BazaarUtils.expandCommandLine(diffCmd, fileSet);
      BazaarDiffConsumer consumer = new BazaarDiffConsumer(this.getLogger(), fileSet.getBasedir());
      ScmResult result = BazaarUtils.execute(consumer, this.getLogger(), fileSet.getBasedir(), diffCmd);
      return new DiffScmResult(consumer.getChangedFiles(), consumer.getDifferences(), consumer.getPatch(), result);
   }
}

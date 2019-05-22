package org.apache.maven.scm.provider.bazaar.command.add;

import java.io.File;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.command.add.AbstractAddCommand;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.bazaar.BazaarUtils;

public class BazaarAddCommand extends AbstractAddCommand implements Command {
   protected ScmResult executeAddCommand(ScmProviderRepository repo, ScmFileSet fileSet, String message, boolean binary) throws ScmException {
      String[] addCmd = new String[]{"add", "--no-recurse"};
      addCmd = BazaarUtils.expandCommandLine(addCmd, fileSet);
      File workingDir = fileSet.getBasedir();
      BazaarAddConsumer consumer = new BazaarAddConsumer(this.getLogger(), workingDir);
      ScmResult result = BazaarUtils.execute(consumer, this.getLogger(), workingDir, addCmd);
      return new AddScmResult(consumer.getAddedFiles(), result);
   }
}

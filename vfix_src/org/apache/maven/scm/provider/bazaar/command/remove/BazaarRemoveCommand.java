package org.apache.maven.scm.provider.bazaar.command.remove;

import java.io.File;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.command.remove.AbstractRemoveCommand;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.bazaar.BazaarUtils;

public class BazaarRemoveCommand extends AbstractRemoveCommand implements Command {
   protected ScmResult executeRemoveCommand(ScmProviderRepository repository, ScmFileSet fileSet, String message) throws ScmException {
      String[] command = new String[]{"remove"};
      BazaarUtils.expandCommandLine(command, fileSet);
      File workingDir = fileSet.getBasedir();
      BazaarRemoveConsumer consumer = new BazaarRemoveConsumer(this.getLogger(), workingDir);
      ScmResult result = BazaarUtils.execute(consumer, this.getLogger(), workingDir, command);
      return new RemoveScmResult(consumer.getRemovedFiles(), result);
   }
}

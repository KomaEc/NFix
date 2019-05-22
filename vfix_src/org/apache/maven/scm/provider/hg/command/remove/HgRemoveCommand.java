package org.apache.maven.scm.provider.hg.command.remove;

import java.io.File;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.command.remove.AbstractRemoveCommand;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.hg.HgUtils;

public class HgRemoveCommand extends AbstractRemoveCommand implements Command {
   protected ScmResult executeRemoveCommand(ScmProviderRepository repository, ScmFileSet fileSet, String message) throws ScmException {
      String[] command = new String[]{"remove"};
      command = HgUtils.expandCommandLine(command, fileSet);
      File workingDir = fileSet.getBasedir();
      HgRemoveConsumer consumer = new HgRemoveConsumer(this.getLogger(), workingDir);
      ScmResult result = HgUtils.execute(consumer, this.getLogger(), workingDir, command);
      return new RemoveScmResult(consumer.getRemovedFiles(), result);
   }
}

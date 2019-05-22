package org.apache.maven.scm.provider.jazz.command.blame;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.blame.AbstractBlameCommand;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.jazz.command.JazzScmCommand;
import org.apache.maven.scm.provider.jazz.command.consumer.ErrorConsumer;

public class JazzBlameCommand extends AbstractBlameCommand {
   public BlameScmResult executeBlameCommand(ScmProviderRepository repo, ScmFileSet fileSet, String filename) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("Executing blame command...");
      }

      JazzScmCommand blameCmd = this.createBlameCommand(repo, fileSet, filename);
      JazzBlameConsumer blameConsumer = new JazzBlameConsumer(repo, this.getLogger());
      ErrorConsumer errConsumer = new ErrorConsumer(this.getLogger());
      int status = blameCmd.execute(blameConsumer, errConsumer);
      return status == 0 && !errConsumer.hasBeenFed() ? new BlameScmResult(blameCmd.getCommandString(), blameConsumer.getLines()) : new BlameScmResult(blameCmd.getCommandString(), "Error code for Jazz SCM blame command - " + status, errConsumer.getOutput(), false);
   }

   public JazzScmCommand createBlameCommand(ScmProviderRepository repo, ScmFileSet fileSet, String filename) {
      JazzScmCommand command = new JazzScmCommand("annotate", (String)null, repo, false, fileSet, this.getLogger());
      command.addArgument(filename);
      return command;
   }
}

package org.apache.maven.scm.provider.tfs.command;

import java.util.ArrayList;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.branch.AbstractBranchCommand;
import org.apache.maven.scm.command.branch.BranchScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.tfs.TfsScmProviderRepository;
import org.apache.maven.scm.provider.tfs.command.consumer.ErrorStreamConsumer;
import org.codehaus.plexus.util.cli.CommandLineUtils;

public class TfsBranchCommand extends AbstractBranchCommand {
   protected ScmResult executeBranchCommand(ScmProviderRepository r, ScmFileSet f, String branch, String message) throws ScmException {
      TfsCommand command = this.createCommand(r, f, branch);
      CommandLineUtils.StringStreamConsumer out = new CommandLineUtils.StringStreamConsumer();
      ErrorStreamConsumer err = new ErrorStreamConsumer();
      int status = command.execute(out, err);
      this.getLogger().info("status of branch command is= " + status + "; err= " + err.getOutput());
      return status == 0 && !err.hasBeenFed() ? new BranchScmResult(command.getCommandString(), new ArrayList(0)) : new BranchScmResult(command.getCommandString(), "Error code for TFS branch command - " + status, err.getOutput(), false);
   }

   public TfsCommand createCommand(ScmProviderRepository r, ScmFileSet f, String branch) {
      TfsCommand command = new TfsCommand("branch", r, f, this.getLogger());
      String serverPath = ((TfsScmProviderRepository)r).getServerPath();
      command.addArgument(serverPath);
      command.addArgument("-checkin");
      command.addArgument(branch);
      return command;
   }
}

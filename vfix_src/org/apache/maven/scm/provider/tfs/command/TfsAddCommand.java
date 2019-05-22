package org.apache.maven.scm.provider.tfs.command;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.add.AbstractAddCommand;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.tfs.command.consumer.ErrorStreamConsumer;
import org.apache.maven.scm.provider.tfs.command.consumer.FileListConsumer;

public class TfsAddCommand extends AbstractAddCommand {
   protected ScmResult executeAddCommand(ScmProviderRepository r, ScmFileSet f, String m, boolean b) throws ScmException {
      TfsCommand command = this.createCommand(r, f);
      FileListConsumer fileConsumer = new FileListConsumer();
      ErrorStreamConsumer err = new ErrorStreamConsumer();
      int status = command.execute(fileConsumer, err);
      return status == 0 && !err.hasBeenFed() ? new AddScmResult(command.getCommandString(), fileConsumer.getFiles()) : new AddScmResult(command.getCommandString(), "Error code for TFS add command - " + status, err.getOutput(), false);
   }

   public TfsCommand createCommand(ScmProviderRepository r, ScmFileSet f) {
      TfsCommand command = new TfsCommand("add", r, f, this.getLogger());
      command.addArgument(f);
      return command;
   }
}

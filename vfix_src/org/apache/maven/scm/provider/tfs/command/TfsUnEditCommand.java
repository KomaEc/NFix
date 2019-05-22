package org.apache.maven.scm.provider.tfs.command;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.unedit.AbstractUnEditCommand;
import org.apache.maven.scm.command.unedit.UnEditScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.tfs.command.consumer.ErrorStreamConsumer;
import org.apache.maven.scm.provider.tfs.command.consumer.FileListConsumer;

public class TfsUnEditCommand extends AbstractUnEditCommand {
   protected ScmResult executeUnEditCommand(ScmProviderRepository r, ScmFileSet f) throws ScmException {
      FileListConsumer out = new FileListConsumer();
      ErrorStreamConsumer err = new ErrorStreamConsumer();
      TfsCommand command = this.createCommand(r, f);
      int status = command.execute(out, err);
      return status == 0 && !err.hasBeenFed() ? new UnEditScmResult(command.getCommandString(), out.getFiles()) : new UnEditScmResult(command.getCommandString(), "Error code for TFS unedit command - " + status, err.getOutput(), false);
   }

   public TfsCommand createCommand(ScmProviderRepository r, ScmFileSet f) {
      TfsCommand command = new TfsCommand("undo", r, f, this.getLogger());
      command.addArgument(f);
      return command;
   }
}

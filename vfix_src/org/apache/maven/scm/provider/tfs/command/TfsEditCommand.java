package org.apache.maven.scm.provider.tfs.command;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.edit.AbstractEditCommand;
import org.apache.maven.scm.command.edit.EditScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.tfs.command.consumer.ErrorStreamConsumer;
import org.apache.maven.scm.provider.tfs.command.consumer.FileListConsumer;

public class TfsEditCommand extends AbstractEditCommand {
   protected ScmResult executeEditCommand(ScmProviderRepository r, ScmFileSet f) throws ScmException {
      FileListConsumer out = new FileListConsumer();
      ErrorStreamConsumer err = new ErrorStreamConsumer();
      TfsCommand command = this.createCommand(r, f);
      int status = command.execute(out, err);
      return status == 0 && !err.hasBeenFed() ? new EditScmResult(command.getCommandString(), out.getFiles()) : new EditScmResult(command.getCommandString(), "Error code for TFS edit command - " + status, err.getOutput(), false);
   }

   protected TfsCommand createCommand(ScmProviderRepository r, ScmFileSet f) {
      TfsCommand command = new TfsCommand("checkout", r, f, this.getLogger());
      command.addArgument(f);
      return command;
   }
}

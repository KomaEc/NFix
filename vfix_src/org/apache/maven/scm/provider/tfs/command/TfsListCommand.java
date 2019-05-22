package org.apache.maven.scm.provider.tfs.command;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.list.AbstractListCommand;
import org.apache.maven.scm.command.list.ListScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.tfs.command.consumer.ErrorStreamConsumer;
import org.apache.maven.scm.provider.tfs.command.consumer.FileListConsumer;
import org.apache.maven.scm.provider.tfs.command.consumer.ServerFileListConsumer;

public class TfsListCommand extends AbstractListCommand {
   protected ListScmResult executeListCommand(ScmProviderRepository r, ScmFileSet f, boolean recursive, ScmVersion v) throws ScmException {
      FileListConsumer out = new ServerFileListConsumer();
      ErrorStreamConsumer err = new ErrorStreamConsumer();
      TfsCommand command = this.createCommand(r, f, recursive);
      int status = command.execute(out, err);
      return status == 0 && !err.hasBeenFed() ? new ListScmResult(command.getCommandString(), out.getFiles()) : new ListScmResult(command.getCommandString(), "Error code for TFS list command - " + status, err.getOutput(), false);
   }

   public TfsCommand createCommand(ScmProviderRepository r, ScmFileSet f, boolean recursive) {
      TfsCommand command = new TfsCommand("dir", r, f, this.getLogger());
      if (recursive) {
         command.addArgument("-recursive");
      }

      command.addArgument(f);
      return command;
   }
}

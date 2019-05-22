package org.apache.maven.scm.provider.tfs.command;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.changelog.ChangeLogCommand;
import org.apache.maven.scm.command.update.AbstractUpdateCommand;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.tfs.TfsScmProviderRepository;
import org.apache.maven.scm.provider.tfs.command.consumer.ErrorStreamConsumer;
import org.apache.maven.scm.provider.tfs.command.consumer.FileListConsumer;

public class TfsUpdateCommand extends AbstractUpdateCommand {
   protected UpdateScmResult executeUpdateCommand(ScmProviderRepository r, ScmFileSet f, ScmVersion v) throws ScmException {
      FileListConsumer fileConsumer = new FileListConsumer();
      ErrorStreamConsumer err = new ErrorStreamConsumer();
      TfsCommand command = this.createCommand(r, f, v);
      int status = command.execute(fileConsumer, err);
      return status == 0 && !err.hasBeenFed() ? new UpdateScmResult(command.getCommandString(), fileConsumer.getFiles()) : new UpdateScmResult(command.getCommandString(), "Error code for TFS update command - " + status, err.getOutput(), false);
   }

   public TfsCommand createCommand(ScmProviderRepository r, ScmFileSet f, ScmVersion v) {
      String serverPath = ((TfsScmProviderRepository)r).getServerPath();
      TfsCommand command = new TfsCommand("get", r, f, this.getLogger());
      command.addArgument(serverPath);
      if (v != null && !v.equals("")) {
         String vType = "";
         if (v.getType().equals("Tag")) {
            vType = "L";
         }

         if (v.getType().equals("Revision")) {
            vType = "C";
         }

         command.addArgument("-version:" + vType + v.getName());
      }

      return command;
   }

   protected ChangeLogCommand getChangeLogCommand() {
      TfsChangeLogCommand changeLogCommand = new TfsChangeLogCommand();
      changeLogCommand.setLogger(this.getLogger());
      return changeLogCommand;
   }
}

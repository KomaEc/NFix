package org.apache.maven.scm.provider.tfs.command;

import java.util.Iterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.status.AbstractStatusCommand;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.tfs.TfsScmProviderRepository;
import org.apache.maven.scm.provider.tfs.command.consumer.ChangedFileConsumer;
import org.apache.maven.scm.provider.tfs.command.consumer.ErrorStreamConsumer;

public class TfsStatusCommand extends AbstractStatusCommand {
   protected StatusScmResult executeStatusCommand(ScmProviderRepository r, ScmFileSet f) throws ScmException {
      TfsScmProviderRepository tfsRepo = (TfsScmProviderRepository)r;
      TfsCommand command = this.createCommand(tfsRepo, f);
      ChangedFileConsumer out = new ChangedFileConsumer(this.getLogger());
      ErrorStreamConsumer err = new ErrorStreamConsumer();
      int status = command.execute(out, err);
      if (status == 0 && !err.hasBeenFed()) {
         Iterator<ScmFile> iter = out.getChangedFiles().iterator();
         this.getLogger().debug("Iterating");

         while(iter.hasNext()) {
            ScmFile file = (ScmFile)iter.next();
            this.getLogger().debug(file.getPath() + ":" + file.getStatus());
         }

         return new StatusScmResult(command.getCommandString(), out.getChangedFiles());
      } else {
         return new StatusScmResult(command.getCommandString(), "Error code for TFS status command - " + status, err.getOutput(), false);
      }
   }

   public TfsCommand createCommand(TfsScmProviderRepository r, ScmFileSet f) {
      String url = r.getServerPath();
      String workspace = r.getWorkspace();
      TfsCommand command = new TfsCommand("status", r, f, this.getLogger());
      if (workspace != null && !workspace.trim().equals("")) {
         command.addArgument("-workspace:" + workspace);
      }

      command.addArgument("-recursive");
      command.addArgument("-format:detailed");
      command.addArgument(url);
      return command;
   }
}

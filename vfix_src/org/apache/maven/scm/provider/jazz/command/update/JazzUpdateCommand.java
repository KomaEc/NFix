package org.apache.maven.scm.provider.jazz.command.update;

import java.util.Iterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.changelog.ChangeLogCommand;
import org.apache.maven.scm.command.update.AbstractUpdateCommand;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.jazz.command.JazzScmCommand;
import org.apache.maven.scm.provider.jazz.command.changelog.JazzChangeLogCommand;
import org.apache.maven.scm.provider.jazz.command.consumer.ErrorConsumer;

public class JazzUpdateCommand extends AbstractUpdateCommand {
   protected UpdateScmResult executeUpdateCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion version) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("Executing update command...");
      }

      JazzUpdateConsumer updateConsumer = new JazzUpdateConsumer(repo, this.getLogger());
      ErrorConsumer err = new ErrorConsumer(this.getLogger());
      JazzScmCommand updateCmd = this.createAcceptCommand(repo, fileSet);
      int status = updateCmd.execute(updateConsumer, err);
      if (status == 0 && !err.hasBeenFed()) {
         if (this.getLogger().isDebugEnabled()) {
            if (!updateConsumer.getUpdatedFiles().isEmpty()) {
               this.getLogger().debug("Iterating over \"Update\" results");
               Iterator i$ = updateConsumer.getUpdatedFiles().iterator();

               while(i$.hasNext()) {
                  ScmFile file = (ScmFile)i$.next();
                  this.getLogger().debug(file.getPath() + " : " + file.getStatus());
               }
            } else {
               this.getLogger().debug("There are no updated files");
            }
         }

         return new UpdateScmResult(updateCmd.getCommandString(), updateConsumer.getUpdatedFiles());
      } else {
         return new UpdateScmResult(updateCmd.getCommandString(), "Error code for Jazz SCM update command - " + status, err.getOutput(), false);
      }
   }

   public JazzScmCommand createAcceptCommand(ScmProviderRepository repo, ScmFileSet fileSet) {
      JazzScmCommand command = new JazzScmCommand("accept", repo, fileSet, this.getLogger());
      command.addArgument("--flow-components");
      return command;
   }

   protected ChangeLogCommand getChangeLogCommand() {
      JazzChangeLogCommand command = new JazzChangeLogCommand();
      command.setLogger(this.getLogger());
      return command;
   }
}

package org.apache.maven.scm.provider.jazz.command.status;

import java.util.Iterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.status.AbstractStatusCommand;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.jazz.command.JazzScmCommand;
import org.apache.maven.scm.provider.jazz.command.consumer.ErrorConsumer;

public class JazzStatusCommand extends AbstractStatusCommand {
   public StatusScmResult executeStatusCommand(ScmProviderRepository repo, ScmFileSet fileSet) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("Executing status command...");
      }

      JazzStatusConsumer statusConsumer = new JazzStatusConsumer(repo, this.getLogger());
      ErrorConsumer errConsumer = new ErrorConsumer(this.getLogger());
      JazzScmCommand statusCmd = this.createStatusCommand(repo, fileSet);
      int status = statusCmd.execute(statusConsumer, errConsumer);
      if (status == 0 && !errConsumer.hasBeenFed()) {
         if (this.getLogger().isDebugEnabled()) {
            if (!statusConsumer.getChangedFiles().isEmpty()) {
               this.getLogger().debug("Iterating over \"Status\" results");
               Iterator i$ = statusConsumer.getChangedFiles().iterator();

               while(i$.hasNext()) {
                  ScmFile file = (ScmFile)i$.next();
                  this.getLogger().debug(file.getPath() + " : " + file.getStatus());
               }
            } else {
               this.getLogger().debug("There are no differences");
            }
         }

         return new StatusScmResult(statusCmd.getCommandString(), statusConsumer.getChangedFiles());
      } else {
         return new StatusScmResult(statusCmd.getCommandString(), "Error code for Jazz SCM status command - " + status, errConsumer.getOutput(), false);
      }
   }

   public JazzScmCommand createStatusCommand(ScmProviderRepository repo, ScmFileSet fileSet) {
      JazzScmCommand command = new JazzScmCommand("status", (String)null, repo, false, fileSet, this.getLogger());
      command.addArgument("--wide");
      return command;
   }
}

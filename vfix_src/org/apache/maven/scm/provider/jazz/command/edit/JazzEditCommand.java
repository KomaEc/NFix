package org.apache.maven.scm.provider.jazz.command.edit;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.edit.AbstractEditCommand;
import org.apache.maven.scm.command.edit.EditScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.jazz.command.JazzScmCommand;
import org.apache.maven.scm.provider.jazz.command.consumer.DebugLoggerConsumer;
import org.apache.maven.scm.provider.jazz.command.consumer.ErrorConsumer;

public class JazzEditCommand extends AbstractEditCommand {
   protected ScmResult executeEditCommand(ScmProviderRepository repo, ScmFileSet fileSet) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("Executing edit command...");
      }

      DebugLoggerConsumer editConsumer = new DebugLoggerConsumer(this.getLogger());
      ErrorConsumer errConsumer = new ErrorConsumer(this.getLogger());
      JazzScmCommand editCmd = this.createEditCommand(repo, fileSet);
      int status = editCmd.execute(editConsumer, errConsumer);
      return status == 0 && !errConsumer.hasBeenFed() ? new EditScmResult(editCmd.getCommandString(), "Successfully Completed.", editConsumer.getOutput(), true) : new EditScmResult(editCmd.getCommandString(), "Error code for Jazz SCM edit command - " + status, errConsumer.getOutput(), false);
   }

   protected JazzScmCommand createEditCommand(ScmProviderRepository repo, ScmFileSet fileSet) {
      JazzScmCommand command = new JazzScmCommand("lock", "acquire", repo, fileSet, this.getLogger());
      List<File> files = fileSet.getFileList();
      if (files != null && !files.isEmpty()) {
         Iterator i$ = files.iterator();

         while(i$.hasNext()) {
            File file = (File)i$.next();
            command.addArgument(file.getPath());
         }
      } else {
         command.addArgument(".");
      }

      return command;
   }
}

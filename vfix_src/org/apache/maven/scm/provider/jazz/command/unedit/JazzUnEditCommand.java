package org.apache.maven.scm.provider.jazz.command.unedit;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.unedit.AbstractUnEditCommand;
import org.apache.maven.scm.command.unedit.UnEditScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.jazz.command.JazzScmCommand;
import org.apache.maven.scm.provider.jazz.command.consumer.DebugLoggerConsumer;
import org.apache.maven.scm.provider.jazz.command.consumer.ErrorConsumer;

public class JazzUnEditCommand extends AbstractUnEditCommand {
   protected ScmResult executeUnEditCommand(ScmProviderRepository repo, ScmFileSet fileSet) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("Executing unedit command...");
      }

      DebugLoggerConsumer uneditConsumer = new DebugLoggerConsumer(this.getLogger());
      ErrorConsumer errConsumer = new ErrorConsumer(this.getLogger());
      JazzScmCommand uneditCmd = this.createUneditCommand(repo, fileSet);
      int status = uneditCmd.execute(uneditConsumer, errConsumer);
      return status == 0 && !errConsumer.hasBeenFed() ? new UnEditScmResult(uneditCmd.getCommandString(), "Successfully Completed.", uneditConsumer.getOutput(), true) : new UnEditScmResult(uneditCmd.getCommandString(), "Error code for Jazz SCM unedit command - " + status, errConsumer.getOutput(), false);
   }

   public JazzScmCommand createUneditCommand(ScmProviderRepository repo, ScmFileSet fileSet) {
      JazzScmCommand command = new JazzScmCommand("lock", "release", repo, fileSet, this.getLogger());
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

package org.apache.maven.scm.provider.clearcase.command.add;

import java.io.File;
import java.util.Iterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.add.AbstractAddCommand;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.clearcase.command.ClearCaseCommand;
import org.apache.maven.scm.provider.clearcase.command.edit.ClearCaseEditCommand;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class ClearCaseAddCommand extends AbstractAddCommand implements ClearCaseCommand {
   protected ScmResult executeAddCommand(ScmProviderRepository scmProviderRepository, ScmFileSet scmFileSet, String string, boolean b) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing add command...");
      }

      Commandline cl = createCommandLine(scmFileSet);
      ClearCaseAddConsumer consumer = new ClearCaseAddConsumer(this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         Commandline checkoutCurrentDirCommandLine = ClearCaseEditCommand.createCheckoutCurrentDirCommandLine(scmFileSet);
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Executing: " + checkoutCurrentDirCommandLine.getWorkingDirectory().getAbsolutePath() + ">>" + checkoutCurrentDirCommandLine.toString());
         }

         exitCode = CommandLineUtils.executeCommandLine(checkoutCurrentDirCommandLine, new CommandLineUtils.StringStreamConsumer(), stderr);
         if (exitCode == 0) {
            if (this.getLogger().isDebugEnabled()) {
               this.getLogger().debug("Executing: " + cl.getWorkingDirectory().getAbsolutePath() + ">>" + cl.toString());
            }

            exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
            if (exitCode == 0) {
               Commandline checkinCurrentDirCommandLine = ClearCaseEditCommand.createCheckinCurrentDirCommandLine(scmFileSet);
               if (this.getLogger().isDebugEnabled()) {
                  this.getLogger().debug("Executing: " + checkinCurrentDirCommandLine.getWorkingDirectory().getAbsolutePath() + ">>" + checkinCurrentDirCommandLine.toString());
               }

               exitCode = CommandLineUtils.executeCommandLine(checkinCurrentDirCommandLine, new CommandLineUtils.StringStreamConsumer(), stderr);
            }
         }
      } catch (CommandLineException var11) {
         throw new ScmException("Error while executing clearcase command.", var11);
      }

      return exitCode != 0 ? new StatusScmResult(cl.toString(), "The cleartool command failed.", stderr.getOutput(), false) : new StatusScmResult(cl.toString(), consumer.getAddedFiles());
   }

   public static Commandline createCommandLine(ScmFileSet scmFileSet) {
      Commandline command = new Commandline();
      File workingDirectory = scmFileSet.getBasedir();
      command.setWorkingDirectory(workingDirectory.getAbsolutePath());
      command.setExecutable("cleartool");
      command.createArg().setValue("mkelem");
      command.createArg().setValue("-c");
      command.createArg().setValue("new file");
      command.createArg().setValue("-nco");
      Iterator i$ = scmFileSet.getFileList().iterator();

      while(i$.hasNext()) {
         File file = (File)i$.next();
         command.createArg().setValue(file.getName());
      }

      return command;
   }
}

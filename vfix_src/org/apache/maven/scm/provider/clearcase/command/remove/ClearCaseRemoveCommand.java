package org.apache.maven.scm.provider.clearcase.command.remove;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.remove.AbstractRemoveCommand;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.clearcase.command.ClearCaseCommand;
import org.apache.maven.scm.provider.clearcase.command.edit.ClearCaseEditCommand;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class ClearCaseRemoveCommand extends AbstractRemoveCommand implements ClearCaseCommand {
   protected ScmResult executeRemoveCommand(ScmProviderRepository scmProviderRepository, ScmFileSet scmFileSet, String string) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing remove command...");
      }

      Commandline cl = createCommandLine(this.getLogger(), scmFileSet);
      ClearCaseRemoveConsumer consumer = new ClearCaseRemoveConsumer(this.getLogger());
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
      } catch (CommandLineException var10) {
         throw new ScmException("Error while executing clearcase command.", var10);
      }

      return exitCode != 0 ? new StatusScmResult(cl.toString(), "The cleartool command failed.", stderr.getOutput(), false) : new StatusScmResult(cl.toString(), consumer.getRemovedFiles());
   }

   public static Commandline createCommandLine(ScmLogger logger, ScmFileSet scmFileSet) {
      Commandline command = new Commandline();
      File workingDirectory = scmFileSet.getBasedir();
      command.setWorkingDirectory(workingDirectory.getAbsolutePath());
      command.setExecutable("cleartool");
      command.createArg().setValue("rmname");
      command.createArg().setValue("-nc");
      List<File> files = scmFileSet.getFileList();

      File file;
      for(Iterator i$ = files.iterator(); i$.hasNext(); command.createArg().setValue(file.getName())) {
         file = (File)i$.next();
         if (logger.isInfoEnabled()) {
            logger.info("Deleting file: " + file.getAbsolutePath());
         }
      }

      return command;
   }
}

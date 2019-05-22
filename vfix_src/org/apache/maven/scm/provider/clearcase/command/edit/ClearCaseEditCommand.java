package org.apache.maven.scm.provider.clearcase.command.edit;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.edit.AbstractEditCommand;
import org.apache.maven.scm.command.edit.EditScmResult;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.clearcase.command.ClearCaseCommand;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class ClearCaseEditCommand extends AbstractEditCommand implements ClearCaseCommand {
   protected ScmResult executeEditCommand(ScmProviderRepository repository, ScmFileSet fileSet) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing edit command...");
      }

      Commandline cl = createCommandLine(this.getLogger(), fileSet);
      ClearCaseEditConsumer consumer = new ClearCaseEditConsumer(this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Executing: " + cl.getWorkingDirectory().getAbsolutePath() + ">>" + cl.toString());
         }

         exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var8) {
         throw new ScmException("Error while executing clearcase command.", var8);
      }

      return exitCode != 0 ? new EditScmResult(cl.toString(), "The cleartool command failed.", stderr.getOutput(), false) : new EditScmResult(cl.toString(), consumer.getEditFiles());
   }

   public static Commandline createCommandLine(ScmLogger logger, ScmFileSet scmFileSet) {
      Commandline command = new Commandline();
      File workingDirectory = scmFileSet.getBasedir();
      command.setWorkingDirectory(workingDirectory.getAbsolutePath());
      command.setExecutable("cleartool");
      command.createArg().setValue("co");
      command.createArg().setValue("-nc");
      List<File> files = scmFileSet.getFileList();

      File file;
      for(Iterator i$ = files.iterator(); i$.hasNext(); command.createArg().setValue(file.getAbsolutePath())) {
         file = (File)i$.next();
         if (logger.isInfoEnabled()) {
            logger.info("edit file: " + file.getAbsolutePath());
         }
      }

      return command;
   }

   public static Commandline createCheckoutCurrentDirCommandLine(ScmFileSet scmFileSet) {
      Commandline command = new Commandline();
      File workingDirectory = scmFileSet.getBasedir();
      command.setWorkingDirectory(workingDirectory.getAbsolutePath());
      command.setExecutable("cleartool");
      command.createArg().setValue("co");
      command.createArg().setValue("-nc");
      command.createArg().setValue(".");
      return command;
   }

   public static Commandline createCheckinCurrentDirCommandLine(ScmFileSet scmFileSet) {
      Commandline command = new Commandline();
      File workingDirectory = scmFileSet.getBasedir();
      command.setWorkingDirectory(workingDirectory.getAbsolutePath());
      command.setExecutable("cleartool");
      command.createArg().setValue("ci");
      command.createArg().setValue("-nc");
      command.createArg().setValue(".");
      return command;
   }
}

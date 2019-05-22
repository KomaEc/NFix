package org.apache.maven.scm.provider.clearcase.command.checkin;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkin.AbstractCheckInCommand;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.clearcase.command.ClearCaseCommand;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class ClearCaseCheckInCommand extends AbstractCheckInCommand implements ClearCaseCommand {
   protected CheckInScmResult executeCheckInCommand(ScmProviderRepository scmProviderRepository, ScmFileSet fileSet, String message, ScmVersion version) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing checkin command...");
      }

      Commandline cl = createCommandLine(fileSet, message);
      ClearCaseCheckInConsumer consumer = new ClearCaseCheckInConsumer(this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Executing: " + cl.getWorkingDirectory().getAbsolutePath() + ">>" + cl.toString());
         }

         exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
      } catch (CommandLineException var10) {
         throw new ScmException("Error while executing clearcase command.", var10);
      }

      return exitCode != 0 ? new CheckInScmResult(cl.toString(), "The cleartool command failed.", stderr.getOutput(), false) : new CheckInScmResult(cl.toString(), consumer.getCheckedInFiles());
   }

   public static Commandline createCommandLine(ScmFileSet scmFileSet, String message) throws ScmException {
      Commandline command = new Commandline();
      File workingDirectory = scmFileSet.getBasedir();
      command.setWorkingDirectory(workingDirectory.getAbsolutePath());
      command.setExecutable("cleartool");
      command.createArg().setValue("ci");
      if (message != null) {
         command.createArg().setValue("-c");
         command.createArg().setLine("\"" + message + "\"");
      } else {
         command.createArg().setValue("-nc");
      }

      List<File> files = scmFileSet.getFileList();
      if (files.isEmpty()) {
         throw new ScmException("There are no files in the fileset to check in!");
      } else {
         Iterator i$ = files.iterator();

         while(i$.hasNext()) {
            File file = (File)i$.next();
            command.createArg().setValue(file.getAbsolutePath());
         }

         return command;
      }
   }
}

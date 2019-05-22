package org.apache.maven.scm.provider.clearcase.command.unedit;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.command.unedit.AbstractUnEditCommand;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.clearcase.command.ClearCaseCommand;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class ClearCaseUnEditCommand extends AbstractUnEditCommand implements ClearCaseCommand {
   protected ScmResult executeUnEditCommand(ScmProviderRepository repository, ScmFileSet fileSet) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing unedit command...");
      }

      Commandline cl = createCommandLine(this.getLogger(), fileSet);
      ClearCaseUnEditConsumer consumer = new ClearCaseUnEditConsumer(this.getLogger());
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

      return exitCode != 0 ? new StatusScmResult(cl.toString(), "The cleartool command failed.", stderr.getOutput(), false) : new StatusScmResult(cl.toString(), consumer.getUnEditFiles());
   }

   public static Commandline createCommandLine(ScmLogger logger, ScmFileSet scmFileSet) {
      Commandline command = new Commandline();
      File workingDirectory = scmFileSet.getBasedir();
      command.setWorkingDirectory(workingDirectory.getAbsolutePath());
      command.setExecutable("cleartool");
      command.createArg().setValue("unco");
      command.createArg().setValue("-keep");
      List<File> files = scmFileSet.getFileList();
      Iterator i$ = files.iterator();

      while(i$.hasNext()) {
         File file = (File)i$.next();
         command.createArg().setValue(file.getName());
      }

      return command;
   }
}

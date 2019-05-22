package org.apache.maven.scm.provider.perforce.command.unedit;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.unedit.AbstractUnEditCommand;
import org.apache.maven.scm.command.unedit.UnEditScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.perforce.PerforceScmProvider;
import org.apache.maven.scm.provider.perforce.command.PerforceCommand;
import org.apache.maven.scm.provider.perforce.repository.PerforceScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class PerforceUnEditCommand extends AbstractUnEditCommand implements PerforceCommand {
   protected ScmResult executeUnEditCommand(ScmProviderRepository repo, ScmFileSet files) throws ScmException {
      Commandline cl = createCommandLine((PerforceScmProviderRepository)repo, files.getBasedir(), files);
      PerforceUnEditConsumer consumer = new PerforceUnEditConsumer();

      try {
         CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();
         int exitCode = CommandLineUtils.executeCommandLine(cl, consumer, err);
         if (exitCode != 0) {
            String cmdLine = CommandLineUtils.toString(cl.getCommandline());
            StringBuilder msg = new StringBuilder("Exit code: " + exitCode + " - " + err.getOutput());
            msg.append('\n');
            msg.append("Command line was:" + cmdLine);
            throw new CommandLineException(msg.toString());
         }
      } catch (CommandLineException var9) {
         if (this.getLogger().isErrorEnabled()) {
            this.getLogger().error("CommandLineException " + var9.getMessage(), var9);
         }
      }

      return consumer.isSuccess() ? new UnEditScmResult(cl.toString(), consumer.getEdits()) : new UnEditScmResult(cl.toString(), "Unable to revert", consumer.getOutput(), consumer.isSuccess());
   }

   public static Commandline createCommandLine(PerforceScmProviderRepository repo, File workingDirectory, ScmFileSet files) {
      Commandline command = PerforceScmProvider.createP4Command(repo, workingDirectory);
      command.createArg().setValue("revert");
      List<File> fs = files.getFileList();
      Iterator i$ = fs.iterator();

      while(i$.hasNext()) {
         File file = (File)i$.next();
         command.createArg().setValue(file.getName());
      }

      return command;
   }
}

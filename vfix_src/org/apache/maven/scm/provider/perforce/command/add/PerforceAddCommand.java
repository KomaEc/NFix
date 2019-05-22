package org.apache.maven.scm.provider.perforce.command.add;

import java.io.File;
import java.util.Iterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.add.AbstractAddCommand;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.perforce.PerforceScmProvider;
import org.apache.maven.scm.provider.perforce.command.PerforceCommand;
import org.apache.maven.scm.provider.perforce.repository.PerforceScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class PerforceAddCommand extends AbstractAddCommand implements PerforceCommand {
   protected ScmResult executeAddCommand(ScmProviderRepository repo, ScmFileSet files, String message, boolean binary) throws ScmException {
      Commandline cl = createCommandLine((PerforceScmProviderRepository)repo, files.getBasedir(), files);
      PerforceAddConsumer consumer = new PerforceAddConsumer();

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
      } catch (CommandLineException var11) {
         if (this.getLogger().isErrorEnabled()) {
            this.getLogger().error("CommandLineException " + var11.getMessage(), var11);
         }
      }

      return new AddScmResult(cl.toString(), consumer.getAdditions());
   }

   public static Commandline createCommandLine(PerforceScmProviderRepository repo, File workingDirectory, ScmFileSet files) {
      Commandline command = PerforceScmProvider.createP4Command(repo, workingDirectory);
      command.createArg().setValue("add");
      Iterator i$ = files.getFileList().iterator();

      while(i$.hasNext()) {
         File file = (File)i$.next();
         command.createArg().setValue(file.getName());
      }

      return command;
   }
}

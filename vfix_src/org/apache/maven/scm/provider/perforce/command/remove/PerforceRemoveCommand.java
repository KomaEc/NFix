package org.apache.maven.scm.provider.perforce.command.remove;

import java.io.File;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.remove.AbstractRemoveCommand;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.perforce.PerforceScmProvider;
import org.apache.maven.scm.provider.perforce.command.PerforceCommand;
import org.apache.maven.scm.provider.perforce.repository.PerforceScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class PerforceRemoveCommand extends AbstractRemoveCommand implements PerforceCommand {
   protected ScmResult executeRemoveCommand(ScmProviderRepository repo, ScmFileSet files, String message) throws ScmException {
      Commandline cl = createCommandLine((PerforceScmProviderRepository)repo, files.getBasedir(), files);
      PerforceRemoveConsumer consumer = new PerforceRemoveConsumer();

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
      } catch (CommandLineException var10) {
         throw new ScmException("CommandLineException " + var10.getMessage(), var10);
      }

      return new RemoveScmResult(cl.toString(), consumer.getRemovals());
   }

   public static Commandline createCommandLine(PerforceScmProviderRepository repo, File workingDirectory, ScmFileSet files) {
      Commandline command = PerforceScmProvider.createP4Command(repo, workingDirectory);
      command.createArg().setValue("delete");
      List<File> fs = files.getFileList();

      for(int i = 0; i < fs.size(); ++i) {
         File file = (File)fs.get(i);
         command.createArg().setValue(file.getName());
      }

      return command;
   }
}

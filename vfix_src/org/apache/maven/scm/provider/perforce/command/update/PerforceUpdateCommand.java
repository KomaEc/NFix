package org.apache.maven.scm.provider.perforce.command.update;

import java.io.File;
import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.changelog.ChangeLogCommand;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.command.update.AbstractUpdateCommand;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.command.update.UpdateScmResultWithRevision;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.perforce.PerforceScmProvider;
import org.apache.maven.scm.provider.perforce.command.PerforceCommand;
import org.apache.maven.scm.provider.perforce.command.changelog.PerforceChangeLogCommand;
import org.apache.maven.scm.provider.perforce.command.checkout.PerforceCheckOutCommand;
import org.apache.maven.scm.provider.perforce.repository.PerforceScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class PerforceUpdateCommand extends AbstractUpdateCommand implements PerforceCommand {
   protected UpdateScmResult executeUpdateCommand(ScmProviderRepository repo, ScmFileSet files, ScmVersion scmVersion) throws ScmException {
      PerforceCheckOutCommand command = new PerforceCheckOutCommand();
      command.setLogger(this.getLogger());
      CommandParameters params = new CommandParameters();
      params.setScmVersion(CommandParameter.SCM_VERSION, scmVersion);
      CheckOutScmResult cosr = (CheckOutScmResult)command.execute(repo, files, params);
      if (!cosr.isSuccess()) {
         return new UpdateScmResult(cosr.getCommandLine(), cosr.getProviderMessage(), cosr.getCommandOutput(), false);
      } else {
         PerforceScmProviderRepository p4repo = (PerforceScmProviderRepository)repo;
         String clientspec = PerforceScmProvider.getClientspecName(this.getLogger(), p4repo, files.getBasedir());
         Commandline cl = createCommandLine(p4repo, files.getBasedir(), clientspec);
         PerforceScmProvider.getRepoPath(this.getLogger(), p4repo, files.getBasedir());
         PerforceHaveConsumer consumer = new PerforceHaveConsumer(this.getLogger());

         try {
            if (this.getLogger().isDebugEnabled()) {
               this.getLogger().debug(PerforceScmProvider.clean("Executing " + cl.toString()));
            }

            CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();
            int exitCode = CommandLineUtils.executeCommandLine(cl, consumer, err);
            if (exitCode != 0) {
               String cmdLine = CommandLineUtils.toString(cl.getCommandline());
               StringBuilder msg = new StringBuilder("Exit code: " + exitCode + " - " + err.getOutput());
               msg.append('\n');
               msg.append("Command line was:" + cmdLine);
               throw new CommandLineException(msg.toString());
            }
         } catch (CommandLineException var16) {
            if (this.getLogger().isErrorEnabled()) {
               this.getLogger().error("CommandLineException " + var16.getMessage(), var16);
            }
         }

         return new UpdateScmResultWithRevision(cosr.getCommandLine(), cosr.getCheckedOutFiles(), String.valueOf(consumer.getHave()));
      }
   }

   protected ChangeLogCommand getChangeLogCommand() {
      PerforceChangeLogCommand command = new PerforceChangeLogCommand();
      command.setLogger(this.getLogger());
      return command;
   }

   public static Commandline createCommandLine(PerforceScmProviderRepository repo, File workingDirectory, String clientspec) {
      Commandline command = PerforceScmProvider.createP4Command(repo, workingDirectory);
      if (clientspec != null) {
         command.createArg().setValue("-c");
         command.createArg().setValue(clientspec);
      }

      command.createArg().setValue("changes");
      command.createArg().setValue("-m1");
      command.createArg().setValue("-ssubmitted");
      command.createArg().setValue("//" + clientspec + "/...#have");
      return command;
   }
}

package org.apache.maven.scm.provider.perforce.command.login;

import java.io.ByteArrayInputStream;
import java.io.File;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.login.AbstractLoginCommand;
import org.apache.maven.scm.command.login.LoginScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.perforce.PerforceScmProvider;
import org.apache.maven.scm.provider.perforce.command.PerforceCommand;
import org.apache.maven.scm.provider.perforce.repository.PerforceScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class PerforceLoginCommand extends AbstractLoginCommand implements PerforceCommand {
   public LoginScmResult executeLoginCommand(ScmProviderRepository repo, ScmFileSet files, CommandParameters params) throws ScmException {
      Commandline cl = createCommandLine((PerforceScmProviderRepository)repo, files.getBasedir());
      PerforceLoginConsumer consumer = new PerforceLoginConsumer();
      boolean isSuccess = false;

      try {
         String password = repo.getPassword();
         if (StringUtils.isEmpty(password)) {
            if (this.getLogger().isInfoEnabled()) {
               this.getLogger().info("No password found, proceeding without it.");
            }

            isSuccess = true;
         } else {
            CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();
            int exitCode = CommandLineUtils.executeCommandLine(cl, new ByteArrayInputStream(password.getBytes()), consumer, err);
            isSuccess = consumer.isSuccess();
            if (!isSuccess) {
               String cmdLine = CommandLineUtils.toString(cl.getCommandline());
               StringBuilder msg = new StringBuilder("Exit code: " + exitCode + " - " + err.getOutput());
               msg.append('\n');
               msg.append("Command line was:" + cmdLine);
               throw new CommandLineException(msg.toString());
            }
         }
      } catch (CommandLineException var12) {
         throw new ScmException(var12.getMessage(), var12);
      }

      return new LoginScmResult(cl.toString(), isSuccess ? "Login successful" : "Login failed", consumer.getOutput(), isSuccess);
   }

   public static Commandline createCommandLine(PerforceScmProviderRepository repo, File workingDir) {
      Commandline command = PerforceScmProvider.createP4Command(repo, workingDir);
      command.createArg().setValue("login");
      if (!StringUtils.isEmpty(repo.getUser())) {
         command.createArg().setValue(repo.getUser());
      }

      return command;
   }
}

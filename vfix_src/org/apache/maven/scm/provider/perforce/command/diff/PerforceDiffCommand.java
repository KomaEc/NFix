package org.apache.maven.scm.provider.perforce.command.diff;

import java.io.File;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.diff.AbstractDiffCommand;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.perforce.PerforceScmProvider;
import org.apache.maven.scm.provider.perforce.command.PerforceCommand;
import org.apache.maven.scm.provider.perforce.repository.PerforceScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class PerforceDiffCommand extends AbstractDiffCommand implements PerforceCommand {
   protected DiffScmResult executeDiffCommand(ScmProviderRepository repo, ScmFileSet files, ScmVersion startRev, ScmVersion endRev) throws ScmException {
      Commandline cl = createCommandLine((PerforceScmProviderRepository)repo, files.getBasedir(), startRev, endRev);
      PerforceDiffConsumer consumer = new PerforceDiffConsumer();
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + PerforceScmProvider.clean(cl.toString()));
      }

      boolean success = false;

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
      } catch (CommandLineException var12) {
         if (this.getLogger().isErrorEnabled()) {
            this.getLogger().error("CommandLineException " + var12.getMessage(), var12);
         }
      }

      return new DiffScmResult(cl.toString(), success ? "Diff successful" : "Unable to diff", consumer.getOutput(), success);
   }

   public static Commandline createCommandLine(PerforceScmProviderRepository repo, File workingDirectory, ScmVersion startRev, ScmVersion endRev) {
      String start = startRev != null && StringUtils.isNotEmpty(startRev.getName()) ? "@" + startRev.getName() : "";
      String end = endRev != null && StringUtils.isNotEmpty(endRev.getName()) ? endRev.getName() : "now";
      Commandline command = PerforceScmProvider.createP4Command(repo, workingDirectory);
      command.createArg().setValue("diff2");
      command.createArg().setValue("-u");
      command.createArg().setValue("..." + start);
      command.createArg().setValue("...@" + end);
      return command;
   }
}

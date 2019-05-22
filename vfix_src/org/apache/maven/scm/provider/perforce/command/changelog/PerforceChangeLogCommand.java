package org.apache.maven.scm.provider.perforce.command.changelog;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.changelog.AbstractChangeLogCommand;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogSet;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.perforce.PerforceScmProvider;
import org.apache.maven.scm.provider.perforce.command.PerforceCommand;
import org.apache.maven.scm.provider.perforce.repository.PerforceScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class PerforceChangeLogCommand extends AbstractChangeLogCommand implements PerforceCommand {
   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion startVersion, ScmVersion endVersion, String datePattern) throws ScmException {
      return this.executeChangeLogCommand(repo, fileSet, (Date)null, (Date)null, (ScmBranch)null, datePattern, startVersion, endVersion);
   }

   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repo, ScmFileSet fileSet, Date startDate, Date endDate, ScmBranch branch, String datePattern) throws ScmException {
      if (branch != null && StringUtils.isNotEmpty(branch.getName())) {
         throw new ScmException("This SCM doesn't support branches.");
      } else {
         return this.executeChangeLogCommand(repo, fileSet, startDate, endDate, branch, datePattern, (ScmVersion)null, (ScmVersion)null);
      }
   }

   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repo, ScmFileSet fileSet, Date startDate, Date endDate, ScmBranch branch, String datePattern, ScmVersion startVersion, ScmVersion endVersion) throws ScmException {
      PerforceScmProviderRepository p4repo = (PerforceScmProviderRepository)repo;
      String clientspec = PerforceScmProvider.getClientspecName(this.getLogger(), p4repo, fileSet.getBasedir());
      Commandline cl = createCommandLine(p4repo, fileSet.getBasedir(), clientspec, (ScmBranch)null, startDate, endDate, startVersion, endVersion);
      String location = PerforceScmProvider.getRepoPath(this.getLogger(), p4repo, fileSet.getBasedir());
      PerforceChangesConsumer consumer = new PerforceChangesConsumer(this.getLogger());

      String change;
      try {
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug(PerforceScmProvider.clean("Executing " + cl.toString()));
         }

         CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();
         int exitCode = CommandLineUtils.executeCommandLine(cl, consumer, err);
         if (exitCode != 0) {
            change = CommandLineUtils.toString(cl.getCommandline());
            StringBuilder msg = new StringBuilder("Exit code: " + exitCode + " - " + err.getOutput());
            msg.append('\n');
            msg.append("Command line was:" + change);
            throw new CommandLineException(msg.toString());
         }
      } catch (CommandLineException var21) {
         if (this.getLogger().isErrorEnabled()) {
            this.getLogger().error("CommandLineException " + var21.getMessage(), var21);
         }
      }

      List<String> changes = consumer.getChanges();
      cl = PerforceScmProvider.createP4Command(p4repo, fileSet.getBasedir());
      cl.createArg().setValue("describe");
      cl.createArg().setValue("-s");
      Iterator i$ = changes.iterator();

      while(i$.hasNext()) {
         change = (String)i$.next();
         cl.createArg().setValue(change);
      }

      PerforceDescribeConsumer describeConsumer = new PerforceDescribeConsumer(location, datePattern, this.getLogger());

      try {
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug(PerforceScmProvider.clean("Executing " + cl.toString()));
         }

         CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();
         int exitCode = CommandLineUtils.executeCommandLine(cl, describeConsumer, err);
         if (exitCode != 0) {
            String cmdLine = CommandLineUtils.toString(cl.getCommandline());
            StringBuilder msg = new StringBuilder("Exit code: " + exitCode + " - " + err.getOutput());
            msg.append('\n');
            msg.append("Command line was:" + cmdLine);
            throw new CommandLineException(msg.toString());
         }
      } catch (CommandLineException var20) {
         if (this.getLogger().isErrorEnabled()) {
            this.getLogger().error("CommandLineException " + var20.getMessage(), var20);
         }
      }

      ChangeLogSet cls = new ChangeLogSet(describeConsumer.getModifications(), (Date)null, (Date)null);
      cls.setStartVersion(startVersion);
      cls.setEndVersion(endVersion);
      return new ChangeLogScmResult(cl.toString(), cls);
   }

   public static Commandline createCommandLine(PerforceScmProviderRepository repo, File workingDirectory, String clientspec, ScmBranch branch, Date startDate, Date endDate, ScmVersion startVersion, ScmVersion endVersion) {
      DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd:HH:mm:ss");
      Commandline command = PerforceScmProvider.createP4Command(repo, workingDirectory);
      if (clientspec != null) {
         command.createArg().setValue("-c");
         command.createArg().setValue(clientspec);
      }

      command.createArg().setValue("changes");
      command.createArg().setValue("-t");
      StringBuilder fileSpec = new StringBuilder("...");
      if (startDate != null) {
         fileSpec.append("@").append(dateFormat.format(startDate)).append(",");
         if (endDate != null) {
            fileSpec.append(dateFormat.format(endDate));
         } else {
            fileSpec.append("now");
         }
      }

      if (startVersion != null) {
         fileSpec.append("@").append(startVersion.getName()).append(",");
         if (endVersion != null) {
            fileSpec.append(endVersion.getName());
         } else {
            fileSpec.append("now");
         }
      }

      command.createArg().setValue(fileSpec.toString());
      return command;
   }
}

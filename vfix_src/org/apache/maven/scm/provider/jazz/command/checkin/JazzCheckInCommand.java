package org.apache.maven.scm.provider.jazz.command.checkin;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.command.checkin.AbstractCheckInCommand;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.jazz.command.JazzScmCommand;
import org.apache.maven.scm.provider.jazz.command.add.JazzAddCommand;
import org.apache.maven.scm.provider.jazz.command.consumer.DebugLoggerConsumer;
import org.apache.maven.scm.provider.jazz.command.consumer.ErrorConsumer;
import org.apache.maven.scm.provider.jazz.repository.JazzScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class JazzCheckInCommand extends AbstractCheckInCommand {
   protected CheckInScmResult executeCheckInCommand(ScmProviderRepository repository, ScmFileSet fileSet, String message, ScmVersion scmVersion) throws ScmException {
      if (scmVersion != null && StringUtils.isNotEmpty(scmVersion.getName())) {
         throw new ScmException("This provider command can't handle tags.");
      } else {
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Executing checkin command...");
         }

         JazzScmCommand createChangesetCmd = this.createCreateChangesetCommand(repository, fileSet, message);
         DebugLoggerConsumer outputConsumer = new DebugLoggerConsumer(this.getLogger());
         ErrorConsumer errConsumer = new ErrorConsumer(this.getLogger());
         int status = createChangesetCmd.execute(outputConsumer, errConsumer);
         return status == 0 && !errConsumer.hasBeenFed() ? this.executeCheckInCommand(repository, fileSet, scmVersion) : new CheckInScmResult(createChangesetCmd.getCommandString(), "Error code for Jazz SCM create changeset command - " + status, errConsumer.getOutput(), false);
      }
   }

   protected CheckInScmResult executeCheckInCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion scmVersion) throws ScmException {
      JazzAddCommand addCommand = new JazzAddCommand();
      addCommand.setLogger(this.getLogger());
      AddScmResult addResult = addCommand.executeAddCommand(repo, fileSet);
      JazzScmProviderRepository jazzRepo = (JazzScmProviderRepository)repo;
      if (jazzRepo.isPushChangesAndHaveFlowTargets()) {
         JazzScmCommand deliverCmd = this.createDeliverCommand((JazzScmProviderRepository)repo, fileSet);
         StreamConsumer deliverConsumer = new DebugLoggerConsumer(this.getLogger());
         ErrorConsumer errConsumer = new ErrorConsumer(this.getLogger());
         int status = deliverCmd.execute(deliverConsumer, errConsumer);
         if (status != 0 || errConsumer.hasBeenFed()) {
            return new CheckInScmResult(deliverCmd.getCommandString(), "Error code for Jazz SCM deliver command - " + status, errConsumer.getOutput(), false);
         }
      }

      return new CheckInScmResult(addResult.getCommandLine(), addResult.getAddedFiles());
   }

   public JazzScmCommand createCreateChangesetCommand(ScmProviderRepository repo, ScmFileSet fileSet, String message) {
      JazzScmCommand command = new JazzScmCommand("create", "changeset", repo, false, fileSet, this.getLogger());
      command.addArgument(message);
      return command;
   }

   public JazzScmCommand createCheckInCommand(ScmProviderRepository repo, ScmFileSet fileSet) {
      JazzScmCommand command = new JazzScmCommand("checkin", (String)null, repo, false, fileSet, this.getLogger());
      List<File> files = fileSet.getFileList();
      if (files != null && !files.isEmpty()) {
         Iterator i$ = files.iterator();

         while(i$.hasNext()) {
            File file = (File)i$.next();
            command.addArgument(file.getPath());
         }
      } else {
         command.addArgument(".");
      }

      return command;
   }

   public JazzScmCommand createDeliverCommand(JazzScmProviderRepository repo, ScmFileSet fileSet) {
      JazzScmCommand command = new JazzScmCommand("deliver", repo, fileSet, this.getLogger());
      if (repo.getWorkspace() != null && !repo.getWorkspace().equals("")) {
         command.addArgument("--source");
         command.addArgument(repo.getWorkspace());
      }

      if (repo.getFlowTarget() != null && !repo.getFlowTarget().equals("")) {
         command.addArgument("--target");
         command.addArgument(repo.getFlowTarget());
      }

      command.addArgument("--overwrite-uncommitted");
      return command;
   }
}

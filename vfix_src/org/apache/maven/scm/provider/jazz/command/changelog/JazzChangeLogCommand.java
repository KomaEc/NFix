package org.apache.maven.scm.provider.jazz.command.changelog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.changelog.AbstractChangeLogCommand;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogSet;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.jazz.command.JazzScmCommand;
import org.apache.maven.scm.provider.jazz.command.consumer.ErrorConsumer;
import org.apache.maven.scm.provider.jazz.repository.JazzScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;

public class JazzChangeLogCommand extends AbstractChangeLogCommand {
   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repo, ScmFileSet fileSet, Date startDate, Date endDate, ScmBranch branch, String datePattern) throws ScmException {
      if (branch != null && StringUtils.isNotEmpty(branch.getName())) {
         throw new ScmException("This SCM provider doesn't support branches.");
      } else {
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Executing changelog command...");
         }

         List<ChangeSet> changeSets = new ArrayList();
         JazzScmCommand historyCommand = this.createHistoryCommand(repo, fileSet);
         JazzHistoryConsumer changeLogConsumer = new JazzHistoryConsumer(repo, this.getLogger(), changeSets);
         ErrorConsumer errConsumer = new ErrorConsumer(this.getLogger());
         int status = historyCommand.execute(changeLogConsumer, errConsumer);
         if (status == 0 && !errConsumer.hasBeenFed()) {
            JazzScmCommand listChangesetsCommand = this.createListChangesetCommand(repo, fileSet, changeSets);
            JazzListChangesetConsumer listChangesetConsumer = new JazzListChangesetConsumer(repo, this.getLogger(), changeSets, datePattern);
            errConsumer = new ErrorConsumer(this.getLogger());
            status = listChangesetsCommand.execute(listChangesetConsumer, errConsumer);
            if (status == 0 && !errConsumer.hasBeenFed()) {
               ChangeLogSet changeLogSet = new ChangeLogSet(changeSets, startDate, endDate);
               return new ChangeLogScmResult(historyCommand.getCommandString(), changeLogSet);
            } else {
               return new ChangeLogScmResult(listChangesetsCommand.getCommandString(), "Error code for Jazz SCM list changesets command - " + status, errConsumer.getOutput(), false);
            }
         } else {
            return new ChangeLogScmResult(historyCommand.getCommandString(), "Error code for Jazz SCM history command - " + status, errConsumer.getOutput(), false);
         }
      }
   }

   protected JazzScmCommand createHistoryCommand(ScmProviderRepository repo, ScmFileSet fileSet) {
      JazzScmCommand command = new JazzScmCommand("history", repo, fileSet, this.getLogger());
      command.addArgument("--maximum");
      command.addArgument("10000000");
      return command;
   }

   protected JazzScmCommand createListChangesetCommand(ScmProviderRepository repo, ScmFileSet fileSet, List<ChangeSet> changeSets) {
      JazzScmProviderRepository jazzRepo = (JazzScmProviderRepository)repo;
      JazzScmCommand command = new JazzScmCommand("list", "changesets", repo, fileSet, this.getLogger());
      command.addArgument("--workspace");
      command.addArgument(jazzRepo.getWorkspace());

      for(int i = 0; i < changeSets.size(); ++i) {
         command.addArgument(((ChangeSet)changeSets.get(i)).getRevision());
      }

      return command;
   }
}

package org.apache.maven.scm.command.update;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.command.changelog.ChangeLogCommand;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogSet;
import org.apache.maven.scm.provider.ScmProviderRepository;

public abstract class AbstractUpdateCommand extends AbstractCommand {
   protected abstract UpdateScmResult executeUpdateCommand(ScmProviderRepository var1, ScmFileSet var2, ScmVersion var3) throws ScmException;

   public ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      ScmVersion scmVersion = parameters.getScmVersion(CommandParameter.SCM_VERSION, (ScmVersion)null);
      boolean runChangelog = Boolean.valueOf(parameters.getString(CommandParameter.RUN_CHANGELOG_WITH_UPDATE, "true"));
      UpdateScmResult updateScmResult = this.executeUpdateCommand(repository, fileSet, scmVersion);
      List<ScmFile> filesList = updateScmResult.getUpdatedFiles();
      if (!runChangelog) {
         return updateScmResult;
      } else {
         ChangeLogCommand changeLogCmd = this.getChangeLogCommand();
         if (filesList != null && filesList.size() > 0 && changeLogCmd != null) {
            ChangeLogScmResult changeLogScmResult = (ChangeLogScmResult)changeLogCmd.executeCommand(repository, fileSet, parameters);
            List<ChangeSet> changes = new ArrayList();
            ChangeLogSet changeLogSet = changeLogScmResult.getChangeLog();
            if (changeLogSet != null) {
               Date startDate = null;

               try {
                  startDate = parameters.getDate(CommandParameter.START_DATE);
               } catch (ScmException var17) {
               }

               Iterator i$ = changeLogSet.getChangeSets().iterator();

               label55:
               while(true) {
                  while(true) {
                     ChangeSet change;
                     do {
                        if (!i$.hasNext()) {
                           break label55;
                        }

                        change = (ChangeSet)i$.next();
                     } while(startDate != null && change.getDate() != null && startDate.after(change.getDate()));

                     Iterator i$ = filesList.iterator();

                     while(i$.hasNext()) {
                        ScmFile currentFile = (ScmFile)i$.next();
                        if (change.containsFilename(currentFile.getPath())) {
                           changes.add(change);
                           break;
                        }
                     }
                  }
               }
            }

            updateScmResult.setChanges(changes);
         }

         return updateScmResult;
      }
   }

   protected abstract ChangeLogCommand getChangeLogCommand();
}

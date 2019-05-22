package org.apache.maven.scm.provider.hg.command.update;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.command.changelog.ChangeLogCommand;
import org.apache.maven.scm.command.update.AbstractUpdateCommand;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.command.update.UpdateScmResultWithRevision;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.hg.HgUtils;
import org.apache.maven.scm.provider.hg.command.HgConsumer;
import org.apache.maven.scm.provider.hg.command.changelog.HgChangeLogCommand;
import org.apache.maven.scm.provider.hg.command.diff.HgDiffConsumer;
import org.codehaus.plexus.util.StringUtils;

public class HgUpdateCommand extends AbstractUpdateCommand implements Command {
   protected UpdateScmResult executeUpdateCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion tag) throws ScmException {
      File workingDir = fileSet.getBasedir();
      String[] updateCmd;
      if (repo.isPushChanges()) {
         updateCmd = new String[]{"pull", "-r", tag != null && !StringUtils.isEmpty(tag.getName()) ? tag.getName() : "tip"};
      } else {
         updateCmd = new String[]{"update", tag != null && !StringUtils.isEmpty(tag.getName()) ? tag.getName() : "tip", "-c"};
      }

      ScmResult updateResult = HgUtils.execute(new HgConsumer(this.getLogger()), this.getLogger(), workingDir, updateCmd);
      if (!updateResult.isSuccess()) {
         return new UpdateScmResult((List)null, (List)null, updateResult);
      } else {
         int currentRevision = HgUtils.getCurrentRevisionNumber(this.getLogger(), workingDir);
         int previousRevision = currentRevision - 1;
         String[] diffCmd = new String[]{"diff", "-r", "" + previousRevision};
         HgDiffConsumer diffConsumer = new HgDiffConsumer(this.getLogger(), workingDir);
         ScmResult diffResult = HgUtils.execute(diffConsumer, this.getLogger(), workingDir, diffCmd);
         List<ScmFile> updatedFiles = new ArrayList();
         List<CharSequence> changes = new ArrayList();
         List<ScmFile> diffFiles = diffConsumer.getChangedFiles();
         Map<String, CharSequence> diffChanges = diffConsumer.getDifferences();
         Iterator i$ = diffFiles.iterator();

         while(i$.hasNext()) {
            ScmFile file = (ScmFile)i$.next();
            changes.add(diffChanges.get(file.getPath()));
            if (file.getStatus() == ScmFileStatus.MODIFIED) {
               updatedFiles.add(new ScmFile(file.getPath(), ScmFileStatus.PATCHED));
            } else {
               updatedFiles.add(file);
            }
         }

         if (repo.isPushChanges()) {
            String[] hgUpdateCmd = new String[]{"update"};
            HgUtils.execute(new HgConsumer(this.getLogger()), this.getLogger(), workingDir, hgUpdateCmd);
         }

         return new UpdateScmResultWithRevision(updatedFiles, new ArrayList(0), String.valueOf(currentRevision), diffResult);
      }
   }

   protected ChangeLogCommand getChangeLogCommand() {
      HgChangeLogCommand command = new HgChangeLogCommand();
      command.setLogger(this.getLogger());
      return command;
   }
}

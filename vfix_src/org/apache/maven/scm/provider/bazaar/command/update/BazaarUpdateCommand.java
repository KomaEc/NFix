package org.apache.maven.scm.provider.bazaar.command.update;

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
import org.apache.maven.scm.provider.bazaar.BazaarUtils;
import org.apache.maven.scm.provider.bazaar.command.BazaarConsumer;
import org.apache.maven.scm.provider.bazaar.command.changelog.BazaarChangeLogCommand;
import org.apache.maven.scm.provider.bazaar.command.diff.BazaarDiffConsumer;
import org.codehaus.plexus.util.StringUtils;

public class BazaarUpdateCommand extends AbstractUpdateCommand implements Command {
   protected UpdateScmResult executeUpdateCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion version) throws ScmException {
      if (version != null && StringUtils.isNotEmpty(version.getName())) {
         throw new ScmException("This provider can't handle tags.");
      } else {
         File workingDir = fileSet.getBasedir();
         String[] updateCmd = new String[]{"pull"};
         ScmResult updateResult = BazaarUtils.execute(new BazaarConsumer(this.getLogger()), this.getLogger(), workingDir, updateCmd);
         if (!updateResult.isSuccess()) {
            return new UpdateScmResult((List)null, (List)null, updateResult);
         } else {
            int currentRevision = BazaarUtils.getCurrentRevisionNumber(this.getLogger(), workingDir);
            int previousRevision = currentRevision - 1;
            String[] diffCmd = new String[]{"diff", "--revision", "" + previousRevision};
            BazaarDiffConsumer diffConsumer = new BazaarDiffConsumer(this.getLogger(), workingDir);
            ScmResult diffResult = BazaarUtils.execute(diffConsumer, this.getLogger(), workingDir, diffCmd);
            List<ScmFile> updatedFiles = new ArrayList();
            List<CharSequence> changes = new ArrayList();
            List<ScmFile> diffFiles = diffConsumer.getChangedFiles();
            Map<String, CharSequence> diffChanges = diffConsumer.getDifferences();
            Iterator it = diffFiles.iterator();

            while(it.hasNext()) {
               ScmFile file = (ScmFile)it.next();
               changes.add(diffChanges.get(file));
               if (file.getStatus() == ScmFileStatus.MODIFIED) {
                  updatedFiles.add(new ScmFile(file.getPath(), ScmFileStatus.PATCHED));
               } else {
                  updatedFiles.add(file);
               }
            }

            return new UpdateScmResultWithRevision(updatedFiles, new ArrayList(0), String.valueOf(currentRevision), diffResult);
         }
      }
   }

   protected ChangeLogCommand getChangeLogCommand() {
      BazaarChangeLogCommand command = new BazaarChangeLogCommand();
      command.setLogger(this.getLogger());
      return command;
   }
}

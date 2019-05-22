package org.apache.maven.scm.provider.bazaar.command.checkin;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkin.AbstractCheckInCommand;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.bazaar.BazaarUtils;
import org.apache.maven.scm.provider.bazaar.command.BazaarConsumer;
import org.apache.maven.scm.provider.bazaar.command.status.BazaarStatusCommand;
import org.apache.maven.scm.provider.bazaar.repository.BazaarScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;

public class BazaarCheckInCommand extends AbstractCheckInCommand {
   protected CheckInScmResult executeCheckInCommand(ScmProviderRepository repo, ScmFileSet fileSet, String message, ScmVersion version) throws ScmException {
      if (version != null && StringUtils.isNotEmpty(version.getName())) {
         throw new ScmException("This provider can't handle tags.");
      } else {
         List<ScmFile> commitedFiles = new ArrayList();
         List<File> files = fileSet.getFileList();
         if (files.isEmpty()) {
            BazaarStatusCommand statusCmd = new BazaarStatusCommand();
            statusCmd.setLogger(this.getLogger());
            StatusScmResult status = statusCmd.executeStatusCommand(repo, fileSet);
            List<ScmFile> statusFiles = status.getChangedFiles();
            Iterator i$ = statusFiles.iterator();

            label44:
            while(true) {
               ScmFile file;
               do {
                  if (!i$.hasNext()) {
                     break label44;
                  }

                  file = (ScmFile)i$.next();
               } while(file.getStatus() != ScmFileStatus.ADDED && file.getStatus() != ScmFileStatus.DELETED && file.getStatus() != ScmFileStatus.MODIFIED);

               commitedFiles.add(new ScmFile(file.getPath(), ScmFileStatus.CHECKED_IN));
            }
         } else {
            Iterator i$ = files.iterator();

            while(i$.hasNext()) {
               File file = (File)i$.next();
               commitedFiles.add(new ScmFile(file.getPath(), ScmFileStatus.CHECKED_IN));
            }
         }

         String[] commitCmd = new String[]{"commit", "--message", message};
         commitCmd = BazaarUtils.expandCommandLine(commitCmd, fileSet);
         ScmResult result = BazaarUtils.execute(new BazaarConsumer(this.getLogger()), this.getLogger(), fileSet.getBasedir(), commitCmd);
         BazaarScmProviderRepository repository = (BazaarScmProviderRepository)repo;
         if (!repository.getURI().equals(fileSet.getBasedir().getAbsolutePath()) && repo.isPushChanges()) {
            String[] pushCmd = new String[]{"push", "--no-strict", repository.getURI()};
            result = BazaarUtils.execute(new BazaarConsumer(this.getLogger()), this.getLogger(), fileSet.getBasedir(), pushCmd);
         }

         return new CheckInScmResult(commitedFiles, result);
      }
   }
}

package org.apache.maven.scm.provider.hg.command.checkin;

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
import org.apache.maven.scm.provider.hg.HgUtils;
import org.apache.maven.scm.provider.hg.command.HgConsumer;
import org.apache.maven.scm.provider.hg.command.status.HgStatusCommand;
import org.apache.maven.scm.provider.hg.repository.HgScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;

public class HgCheckInCommand extends AbstractCheckInCommand {
   protected CheckInScmResult executeCheckInCommand(ScmProviderRepository repo, ScmFileSet fileSet, String message, ScmVersion tag) throws ScmException {
      if (tag != null && !StringUtils.isEmpty(tag.getName())) {
         throw new ScmException("This provider can't handle tags for this operation");
      } else {
         File workingDir = fileSet.getBasedir();
         String branchName = HgUtils.getCurrentBranchName(this.getLogger(), workingDir);
         boolean differentOutgoingBranch = repo.isPushChanges() ? HgUtils.differentOutgoingBranchFound(this.getLogger(), workingDir, branchName) : false;
         List<ScmFile> commitedFiles = new ArrayList();
         List<File> files = fileSet.getFileList();
         if (files.isEmpty()) {
            HgStatusCommand statusCmd = new HgStatusCommand();
            statusCmd.setLogger(this.getLogger());
            StatusScmResult status = statusCmd.executeStatusCommand(repo, fileSet);
            List<ScmFile> statusFiles = status.getChangedFiles();
            Iterator i$ = statusFiles.iterator();

            label54:
            while(true) {
               ScmFile file;
               do {
                  if (!i$.hasNext()) {
                     break label54;
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
         commitCmd = HgUtils.expandCommandLine(commitCmd, fileSet);
         ScmResult result = HgUtils.execute(new HgConsumer(this.getLogger()), this.getLogger(), fileSet.getBasedir(), commitCmd);
         HgScmProviderRepository repository = (HgScmProviderRepository)repo;
         if (repo.isPushChanges()) {
            if (!repository.getURI().equals(fileSet.getBasedir().getAbsolutePath())) {
               String[] pushCmd = new String[]{"push", differentOutgoingBranch ? "-r" + branchName : null, repository.getURI()};
               result = HgUtils.execute(new HgConsumer(this.getLogger()), this.getLogger(), fileSet.getBasedir(), pushCmd);
            }

            return new CheckInScmResult(commitedFiles, result);
         } else {
            return new CheckInScmResult(commitedFiles, result);
         }
      }
   }
}

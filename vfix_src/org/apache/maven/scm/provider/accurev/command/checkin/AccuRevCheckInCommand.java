package org.apache.maven.scm.provider.accurev.command.checkin;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.accurev.AccuRev;
import org.apache.maven.scm.provider.accurev.AccuRevException;
import org.apache.maven.scm.provider.accurev.AccuRevInfo;
import org.apache.maven.scm.provider.accurev.AccuRevScmProviderRepository;
import org.apache.maven.scm.provider.accurev.command.AbstractAccuRevCommand;

public class AccuRevCheckInCommand extends AbstractAccuRevCommand {
   public AccuRevCheckInCommand(ScmLogger logger) {
      super(logger);
   }

   protected ScmResult executeAccurevCommand(AccuRevScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException, AccuRevException {
      AccuRev accuRev = repository.getAccuRev();
      String message = parameters.getString(CommandParameter.MESSAGE);
      List<File> promotedFiles = null;
      File basedir = fileSet.getBasedir();
      List<File> fileList = fileSet.getFileList();
      if (fileList.isEmpty()) {
         AccuRevInfo info = accuRev.info(basedir);
         if (!repository.isWorkSpaceRoot(info)) {
            throw new ScmException(String.format("Unsupported recursive checkin for %s. Not the workspace root", basedir.getAbsolutePath()));
         }

         promotedFiles = accuRev.promoteAll(basedir, message);
      } else {
         promotedFiles = accuRev.promote(basedir, fileList, message);
      }

      if (promotedFiles != null) {
         Iterator iter = promotedFiles.iterator();

         while(iter.hasNext()) {
            if ((new File(basedir, ((File)iter.next()).getPath())).isDirectory()) {
               iter.remove();
            }
         }

         return new CheckInScmResult(accuRev.getCommandLines(), getScmFiles(promotedFiles, ScmFileStatus.CHECKED_IN));
      } else {
         return new CheckInScmResult(accuRev.getCommandLines(), "AccuRev Error", accuRev.getErrorOutput(), false);
      }
   }

   public CheckInScmResult checkIn(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (CheckInScmResult)this.execute(repository, fileSet, parameters);
   }
}

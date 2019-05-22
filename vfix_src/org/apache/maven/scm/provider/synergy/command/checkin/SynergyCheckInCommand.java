package org.apache.maven.scm.provider.synergy.command.checkin;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkin.AbstractCheckInCommand;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.synergy.command.SynergyCommand;
import org.apache.maven.scm.provider.synergy.repository.SynergyScmProviderRepository;
import org.apache.maven.scm.provider.synergy.util.SynergyRole;
import org.apache.maven.scm.provider.synergy.util.SynergyTaskManager;
import org.apache.maven.scm.provider.synergy.util.SynergyUtil;

public class SynergyCheckInCommand extends AbstractCheckInCommand implements SynergyCommand {
   protected CheckInScmResult executeCheckInCommand(ScmProviderRepository repository, ScmFileSet fileSet, String message, ScmVersion version) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing checkin command...");
      }

      SynergyScmProviderRepository repo = (SynergyScmProviderRepository)repository;
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug(fileSet.toString());
      }

      String ccmAddr = SynergyUtil.start(this.getLogger(), repo.getUser(), repo.getPassword(), (SynergyRole)null);

      try {
         SynergyTaskManager.getInstance().checkinDefaultTask(this.getLogger(), message, ccmAddr);
      } finally {
         SynergyUtil.stop(this.getLogger(), ccmAddr);
      }

      ArrayList scmFiles = new ArrayList(fileSet.getFileList().size());
      Iterator i$ = fileSet.getFileList().iterator();

      while(i$.hasNext()) {
         File f = (File)i$.next();
         scmFiles.add(new ScmFile(f.getPath(), ScmFileStatus.CHECKED_IN));
      }

      return new CheckInScmResult("ccm checkin", scmFiles);
   }
}

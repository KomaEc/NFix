package org.apache.maven.scm.provider.synergy.command.status;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.command.status.AbstractStatusCommand;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.synergy.command.SynergyCommand;
import org.apache.maven.scm.provider.synergy.repository.SynergyScmProviderRepository;
import org.apache.maven.scm.provider.synergy.util.SynergyRole;
import org.apache.maven.scm.provider.synergy.util.SynergyUtil;

public class SynergyStatusCommand extends AbstractStatusCommand implements SynergyCommand {
   protected StatusScmResult executeStatusCommand(ScmProviderRepository repository, ScmFileSet fileSet) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing status command...");
      }

      SynergyScmProviderRepository repo = (SynergyScmProviderRepository)repository;
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("basedir: " + fileSet.getBasedir());
      }

      String ccmAddr = SynergyUtil.start(this.getLogger(), repo.getUser(), repo.getPassword(), (SynergyRole)null);

      List l;
      try {
         l = SynergyUtil.getWorkingFiles(this.getLogger(), repo.getProjectSpec(), repo.getProjectRelease(), ccmAddr);
      } finally {
         SynergyUtil.stop(this.getLogger(), ccmAddr);
      }

      LinkedList result = new LinkedList();
      Iterator i$ = l.iterator();

      while(i$.hasNext()) {
         String filename = (String)i$.next();
         ScmFile f = new ScmFile(filename, ScmFileStatus.MODIFIED);
         result.add(f);
      }

      return new StatusScmResult("ccm dir", result);
   }
}

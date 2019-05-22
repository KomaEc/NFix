package org.apache.maven.scm.provider.synergy.command.remove;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.remove.AbstractRemoveCommand;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.synergy.command.SynergyCommand;
import org.apache.maven.scm.provider.synergy.repository.SynergyScmProviderRepository;
import org.apache.maven.scm.provider.synergy.util.SynergyRole;
import org.apache.maven.scm.provider.synergy.util.SynergyUtil;

public class SynergyRemoveCommand extends AbstractRemoveCommand implements SynergyCommand {
   protected ScmResult executeRemoveCommand(ScmProviderRepository repository, ScmFileSet fileSet, String message) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing remove command...");
      }

      SynergyScmProviderRepository repo = (SynergyScmProviderRepository)repository;
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("basedir: " + fileSet.getBasedir());
      }

      String ccmAddr = SynergyUtil.start(this.getLogger(), repo.getUser(), repo.getPassword(), (SynergyRole)null);

      File destPath;
      try {
         String projectSpec = SynergyUtil.getWorkingProject(this.getLogger(), repo.getProjectSpec(), repo.getUser(), ccmAddr);
         if (projectSpec == null) {
            throw new ScmException("You should checkout a working project first");
         }

         File waPath = SynergyUtil.getWorkArea(this.getLogger(), projectSpec, ccmAddr);
         destPath = new File(waPath, repo.getProjectName());
         Iterator i$ = fileSet.getFileList().iterator();

         while(i$.hasNext()) {
            File f = (File)i$.next();
            File source = new File(fileSet.getBasedir(), f.getPath());
            File dest = new File(destPath, f.getPath());
            SynergyUtil.delete(this.getLogger(), dest, ccmAddr, false);
            if (!source.equals(dest)) {
               if (this.getLogger().isDebugEnabled()) {
                  this.getLogger().debug("Delete file [" + source + "].");
               }

               dest.delete();
            }
         }
      } finally {
         SynergyUtil.stop(this.getLogger(), ccmAddr);
      }

      ArrayList scmFiles = new ArrayList();
      Iterator i$ = fileSet.getFileList().iterator();

      while(i$.hasNext()) {
         destPath = (File)i$.next();
         scmFiles.add(new ScmFile(destPath.getPath(), ScmFileStatus.DELETED));
      }

      return new StatusScmResult("", scmFiles);
   }
}

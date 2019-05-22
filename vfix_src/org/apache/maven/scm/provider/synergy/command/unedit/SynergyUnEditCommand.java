package org.apache.maven.scm.provider.synergy.command.unedit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.unedit.AbstractUnEditCommand;
import org.apache.maven.scm.command.unedit.UnEditScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.synergy.command.SynergyCommand;
import org.apache.maven.scm.provider.synergy.repository.SynergyScmProviderRepository;
import org.apache.maven.scm.provider.synergy.util.SynergyRole;
import org.apache.maven.scm.provider.synergy.util.SynergyUtil;
import org.codehaus.plexus.util.FileUtils;

public class SynergyUnEditCommand extends AbstractUnEditCommand implements SynergyCommand {
   protected ScmResult executeUnEditCommand(ScmProviderRepository repository, ScmFileSet fileSet) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing unedit command...");
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
            SynergyUtil.delete(this.getLogger(), dest, ccmAddr, true);
            if (!source.equals(dest)) {
               if (this.getLogger().isDebugEnabled()) {
                  this.getLogger().debug("Copy file [" + dest + "] to [" + source + "].");
               }

               try {
                  FileUtils.copyFile(dest, source);
               } catch (IOException var16) {
                  throw new ScmException("Unable to restore file in output folder", var16);
               }
            }
         }
      } finally {
         SynergyUtil.stop(this.getLogger(), ccmAddr);
      }

      ArrayList files = new ArrayList();
      Iterator i$ = fileSet.getFileList().iterator();

      while(i$.hasNext()) {
         destPath = (File)i$.next();
         files.add(new ScmFile(destPath.getPath(), ScmFileStatus.UNKNOWN));
      }

      return new UnEditScmResult("", files);
   }
}

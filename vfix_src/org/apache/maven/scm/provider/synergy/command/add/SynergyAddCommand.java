package org.apache.maven.scm.provider.synergy.command.add;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.add.AbstractAddCommand;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.synergy.command.SynergyCommand;
import org.apache.maven.scm.provider.synergy.repository.SynergyScmProviderRepository;
import org.apache.maven.scm.provider.synergy.util.SynergyRole;
import org.apache.maven.scm.provider.synergy.util.SynergyUtil;
import org.codehaus.plexus.util.FileUtils;

public class SynergyAddCommand extends AbstractAddCommand implements SynergyCommand {
   protected ScmResult executeAddCommand(ScmProviderRepository repository, ScmFileSet fileSet, String message, boolean binary) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing add command...");
      }

      SynergyScmProviderRepository repo = (SynergyScmProviderRepository)repository;
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("basedir: " + fileSet.getBasedir());
      }

      if (message == null || message.equals("")) {
         message = "Maven SCM Synergy provider: adding file(s) to project " + repo.getProjectSpec();
      }

      String ccmAddr = SynergyUtil.start(this.getLogger(), repo.getUser(), repo.getPassword(), (SynergyRole)null);

      File f;
      try {
         int taskNum = SynergyUtil.createTask(this.getLogger(), message, repo.getProjectRelease(), true, ccmAddr);
         String projectSpec = SynergyUtil.getWorkingProject(this.getLogger(), repo.getProjectSpec(), repo.getUser(), ccmAddr);
         if (projectSpec == null) {
            throw new ScmException("You should checkout a working project first");
         }

         f = SynergyUtil.getWorkArea(this.getLogger(), projectSpec, ccmAddr);
         File destPath = new File(f, repo.getProjectName());
         Iterator i$ = fileSet.getFileList().iterator();

         while(true) {
            if (!i$.hasNext()) {
               SynergyUtil.checkinTask(this.getLogger(), taskNum, message, ccmAddr);
               break;
            }

            File source = (File)i$.next();
            File dest = new File(destPath, SynergyUtil.removePrefix(fileSet.getBasedir(), source));
            if (!source.equals(dest)) {
               if (this.getLogger().isDebugEnabled()) {
                  this.getLogger().debug("Copy file [" + source + "] to Synergy Work Area [" + dest + "].");
               }

               try {
                  FileUtils.copyFile(source, dest);
               } catch (IOException var18) {
                  throw new ScmException("Unable to copy file in Work Area", var18);
               }
            }

            SynergyUtil.create(this.getLogger(), dest, message, ccmAddr);
         }
      } finally {
         SynergyUtil.stop(this.getLogger(), ccmAddr);
      }

      ArrayList scmFiles = new ArrayList(fileSet.getFileList().size());
      Iterator i$ = fileSet.getFileList().iterator();

      while(i$.hasNext()) {
         f = (File)i$.next();
         scmFiles.add(new ScmFile(f.getPath(), ScmFileStatus.ADDED));
      }

      return new AddScmResult("", scmFiles);
   }
}

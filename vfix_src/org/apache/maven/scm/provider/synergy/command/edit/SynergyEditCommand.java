package org.apache.maven.scm.provider.synergy.command.edit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.edit.AbstractEditCommand;
import org.apache.maven.scm.command.edit.EditScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.synergy.command.SynergyCommand;
import org.apache.maven.scm.provider.synergy.repository.SynergyScmProviderRepository;
import org.apache.maven.scm.provider.synergy.util.SynergyRole;
import org.apache.maven.scm.provider.synergy.util.SynergyUtil;
import org.codehaus.plexus.util.FileUtils;

public class SynergyEditCommand extends AbstractEditCommand implements SynergyCommand {
   protected ScmResult executeEditCommand(ScmProviderRepository repository, ScmFileSet fileSet) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing edit command...");
      }

      SynergyScmProviderRepository repo = (SynergyScmProviderRepository)repository;
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug(fileSet.toString());
      }

      String ccmAddr = SynergyUtil.start(this.getLogger(), repo.getUser(), repo.getPassword(), (SynergyRole)null);

      File sourcePath;
      try {
         String projectSpec = SynergyUtil.getWorkingProject(this.getLogger(), repo.getProjectSpec(), repo.getUser(), ccmAddr);
         File waPath = SynergyUtil.getWorkArea(this.getLogger(), projectSpec, ccmAddr);
         sourcePath = new File(waPath, repo.getProjectName());
         if (projectSpec == null) {
            throw new ScmException("You should checkout project first");
         }

         int taskNum = SynergyUtil.createTask(this.getLogger(), "Maven SCM Synergy provider: edit command for project " + repo.getProjectSpec(), repo.getProjectRelease(), true, ccmAddr);
         if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info("Task " + taskNum + " was created to perform checkout.");
         }

         Iterator i$ = fileSet.getFileList().iterator();

         while(i$.hasNext()) {
            File f = (File)i$.next();
            File dest = f;
            File source = new File(sourcePath, SynergyUtil.removePrefix(fileSet.getBasedir(), f));
            List<File> list = new LinkedList();
            list.add(source);
            SynergyUtil.checkoutFiles(this.getLogger(), list, ccmAddr);
            if (!source.equals(f)) {
               if (this.getLogger().isDebugEnabled()) {
                  this.getLogger().debug("Copy file [" + source + "] to expected folder [" + f + "].");
               }

               try {
                  FileUtils.copyFile(source, dest);
               } catch (IOException var18) {
                  throw new ScmException("Unable to copy file from Work Area", var18);
               }
            }
         }
      } finally {
         SynergyUtil.stop(this.getLogger(), ccmAddr);
      }

      ArrayList scmFiles = new ArrayList(fileSet.getFileList().size());
      Iterator i$ = fileSet.getFileList().iterator();

      while(i$.hasNext()) {
         sourcePath = (File)i$.next();
         scmFiles.add(new ScmFile(sourcePath.getPath(), ScmFileStatus.EDITED));
      }

      return new EditScmResult("", scmFiles);
   }
}

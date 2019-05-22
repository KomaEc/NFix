package org.apache.maven.scm.provider.synergy.command.checkout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkout.AbstractCheckOutCommand;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.synergy.command.SynergyCommand;
import org.apache.maven.scm.provider.synergy.repository.SynergyScmProviderRepository;
import org.apache.maven.scm.provider.synergy.util.SynergyRole;
import org.apache.maven.scm.provider.synergy.util.SynergyUtil;
import org.codehaus.plexus.util.FileUtils;

public class SynergyCheckOutCommand extends AbstractCheckOutCommand implements SynergyCommand {
   protected CheckOutScmResult executeCheckOutCommand(ScmProviderRepository repository, ScmFileSet fileSet, ScmVersion version, boolean recursive) throws ScmException {
      if (fileSet.getFileList().size() != 0) {
         throw new ScmException("This provider doesn't support checking out subsets of a project");
      } else {
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("executing checkout command...");
         }

         SynergyScmProviderRepository repo = (SynergyScmProviderRepository)repository;
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug(fileSet.toString());
         }

         String ccmAddr = SynergyUtil.start(this.getLogger(), repo.getUser(), repo.getPassword(), (SynergyRole)null);

         File waPath;
         try {
            String projectSpec = SynergyUtil.getWorkingProject(this.getLogger(), repo.getProjectSpec(), repo.getUser(), ccmAddr);
            if (projectSpec != null) {
               if (this.getLogger().isInfoEnabled()) {
                  this.getLogger().info("A working project already exists [" + projectSpec + "].");
               }

               SynergyUtil.synchronize(this.getLogger(), projectSpec, ccmAddr);
            } else {
               SynergyUtil.checkoutProject(this.getLogger(), (File)null, repo.getProjectSpec(), version, repo.getProjectPurpose(), repo.getProjectRelease(), ccmAddr);
               projectSpec = SynergyUtil.getWorkingProject(this.getLogger(), repo.getProjectSpec(), repo.getUser(), ccmAddr);
               if (this.getLogger().isInfoEnabled()) {
                  this.getLogger().info("A new working project [" + projectSpec + "] was created.");
               }
            }

            SynergyUtil.reconfigure(this.getLogger(), projectSpec, ccmAddr);
            waPath = SynergyUtil.getWorkArea(this.getLogger(), projectSpec, ccmAddr);
         } finally {
            SynergyUtil.stop(this.getLogger(), ccmAddr);
         }

         File source = new File(waPath, repo.getProjectName());
         if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info("We will now copy files from Synergy Work Area [" + source + "] to expected folder [" + fileSet.getBasedir() + "]");
         }

         try {
            FileUtils.copyDirectoryStructure(source, fileSet.getBasedir());
         } catch (IOException var16) {
            throw new ScmException("Unable to copy directory structure", var16);
         }

         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("We will list content of checkout directory.");
         }

         ArrayList files = new ArrayList();

         try {
            List<File> realFiles = FileUtils.getFiles(fileSet.getBasedir(), (String)null, "_ccmwaid.inf");
            Iterator i$ = realFiles.iterator();

            while(i$.hasNext()) {
               File f = (File)i$.next();
               files.add(new ScmFile(f.getPath(), ScmFileStatus.CHECKED_OUT));
            }
         } catch (IOException var18) {
            throw new ScmException("Unable to list files in checkout directory", var18);
         }

         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("checkout command end successfully ...");
         }

         return new CheckOutScmResult(files, new ScmResult("multiple commandline", "OK", "OK", true));
      }
   }
}

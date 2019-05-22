package org.apache.maven.scm.provider.synergy.command.update;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.changelog.ChangeLogCommand;
import org.apache.maven.scm.command.update.AbstractUpdateCommand;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.synergy.command.SynergyCommand;
import org.apache.maven.scm.provider.synergy.command.changelog.SynergyChangeLogCommand;
import org.apache.maven.scm.provider.synergy.repository.SynergyScmProviderRepository;
import org.apache.maven.scm.provider.synergy.util.SynergyRole;
import org.apache.maven.scm.provider.synergy.util.SynergyUtil;
import org.codehaus.plexus.util.FileUtils;

public class SynergyUpdateCommand extends AbstractUpdateCommand implements SynergyCommand {
   protected UpdateScmResult executeUpdateCommand(ScmProviderRepository repository, ScmFileSet fileSet, ScmVersion version) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing update command...");
      }

      SynergyScmProviderRepository repo = (SynergyScmProviderRepository)repository;
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("basedir: " + fileSet.getBasedir());
      }

      String ccmAddr = SynergyUtil.start(this.getLogger(), repo.getUser(), repo.getPassword(), (SynergyRole)null);

      File waPath;
      try {
         String projectSpec = SynergyUtil.getWorkingProject(this.getLogger(), repo.getProjectSpec(), repo.getUser(), ccmAddr);
         SynergyUtil.reconfigureProperties(this.getLogger(), projectSpec, ccmAddr);
         SynergyUtil.reconfigure(this.getLogger(), projectSpec, ccmAddr);
         waPath = SynergyUtil.getWorkArea(this.getLogger(), projectSpec, ccmAddr);
      } finally {
         SynergyUtil.stop(this.getLogger(), ccmAddr);
      }

      File source = new File(waPath, repo.getProjectName());
      ArrayList modifications = new ArrayList();
      if (!source.equals(fileSet.getBasedir())) {
         if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info("We will copy modified files from Synergy Work Area [" + source + "] to expected folder [" + fileSet.getBasedir() + "]");
         }

         try {
            copyDirectoryStructure(source, fileSet.getBasedir(), modifications);
         } catch (IOException var12) {
            throw new ScmException("Unable to copy directory structure", var12);
         }
      }

      return new UpdateScmResult("ccm reconcile -uwa ...", modifications);
   }

   protected ChangeLogCommand getChangeLogCommand() {
      SynergyChangeLogCommand changeLogCmd = new SynergyChangeLogCommand();
      changeLogCmd.setLogger(this.getLogger());
      return changeLogCmd;
   }

   public static void copyDirectoryStructure(File sourceDirectory, File destinationDirectory, List<ScmFile> modifications) throws IOException {
      if (!sourceDirectory.exists()) {
         throw new IOException("Source directory doesn't exists (" + sourceDirectory.getAbsolutePath() + ").");
      } else {
         File[] files = sourceDirectory.listFiles();
         String sourcePath = sourceDirectory.getAbsolutePath();
         File[] arr$ = files;
         int len$ = files.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            File file = arr$[i$];
            String dest = file.getAbsolutePath();
            dest = dest.substring(sourcePath.length() + 1);
            File destination = new File(destinationDirectory, dest);
            if (file.isFile()) {
               if (file.lastModified() != destination.lastModified()) {
                  destination = destination.getParentFile();
                  FileUtils.copyFileToDirectory(file, destination);
                  modifications.add(new ScmFile(file.getAbsolutePath(), ScmFileStatus.UPDATED));
               }
            } else {
               if (!file.isDirectory()) {
                  throw new IOException("Unknown file type: " + file.getAbsolutePath());
               }

               if (!destination.exists() && !destination.mkdirs()) {
                  throw new IOException("Could not create destination directory '" + destination.getAbsolutePath() + "'.");
               }

               copyDirectoryStructure(file, destination, modifications);
            }
         }

      }
   }
}

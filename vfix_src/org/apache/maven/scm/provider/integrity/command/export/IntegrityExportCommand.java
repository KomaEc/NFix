package org.apache.maven.scm.provider.integrity.command.export;

import com.mks.api.response.APIException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.export.AbstractExportCommand;
import org.apache.maven.scm.command.export.ExportScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.ExceptionHandler;
import org.apache.maven.scm.provider.integrity.Member;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;

public class IntegrityExportCommand extends AbstractExportCommand {
   public ExportScmResult executeExportCommand(ScmProviderRepository repository, ScmFileSet fileSet, ScmVersion scmVersion, String outputDirectory) throws ScmException {
      String exportDir = null != outputDirectory && outputDirectory.length() > 0 ? outputDirectory : fileSet.getBasedir().getAbsolutePath();
      this.getLogger().info("Attempting to export files to " + exportDir);
      IntegrityScmProviderRepository iRepo = (IntegrityScmProviderRepository)repository;

      ExportScmResult result;
      try {
         boolean exportSuccess = true;
         List<Member> projectMembers = iRepo.getProject().listFiles(exportDir);
         List<ScmFile> scmFileList = new ArrayList();
         Iterator it = projectMembers.iterator();

         while(it.hasNext()) {
            Member siMember = (Member)it.next();

            try {
               this.getLogger().info("Attempting to export file: " + siMember.getTargetFilePath() + " at revision " + siMember.getRevision());
               siMember.checkout(iRepo.getAPISession());
               scmFileList.add(new ScmFile(siMember.getTargetFilePath(), ScmFileStatus.UNKNOWN));
            } catch (APIException var15) {
               exportSuccess = false;
               ExceptionHandler eh = new ExceptionHandler(var15);
               this.getLogger().error("MKS API Exception: " + eh.getMessage());
               this.getLogger().debug(eh.getCommand() + " exited with return code " + eh.getExitCode());
            }
         }

         this.getLogger().info("Exported " + scmFileList.size() + " files out of a total of " + projectMembers.size() + " files!");
         if (exportSuccess) {
            result = new ExportScmResult("si co", scmFileList);
         } else {
            result = new ExportScmResult("si co", "Failed to export all files!", "", exportSuccess);
         }
      } catch (APIException var16) {
         ExceptionHandler eh = new ExceptionHandler(var16);
         this.getLogger().error("MKS API Exception: " + eh.getMessage());
         this.getLogger().debug(eh.getCommand() + " exited with return code " + eh.getExitCode());
         result = new ExportScmResult(eh.getCommand(), eh.getMessage(), "Exit Code: " + eh.getExitCode(), false);
      }

      return result;
   }
}

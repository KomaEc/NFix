package org.apache.maven.scm.provider.integrity.command.status;

import com.mks.api.response.APIException;
import com.mks.api.response.Item;
import com.mks.api.response.WorkItem;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.status.AbstractStatusCommand;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.ExceptionHandler;
import org.apache.maven.scm.provider.integrity.Sandbox;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;

public class IntegrityStatusCommand extends AbstractStatusCommand {
   public StatusScmResult executeStatusCommand(ScmProviderRepository repository, ScmFileSet fileSet) throws ScmException {
      IntegrityScmProviderRepository iRepo = (IntegrityScmProviderRepository)repository;
      this.getLogger().info("Status of files changed in sandbox " + fileSet.getBasedir().getAbsolutePath());

      StatusScmResult result;
      try {
         List<ScmFile> scmFileList = new ArrayList();
         Sandbox siSandbox = iRepo.getSandbox();
         String excludes = Sandbox.formatFilePatterns(fileSet.getExcludes());
         String includes = Sandbox.formatFilePatterns(fileSet.getIncludes());
         List<ScmFile> newMemberList = siSandbox.getNewMembers(excludes, includes);
         scmFileList.addAll(newMemberList);
         List<WorkItem> changeList = siSandbox.getChangeList();
         Iterator wit = changeList.iterator();

         while(wit.hasNext()) {
            WorkItem wi = (WorkItem)wit.next();
            File memberFile = new File(wi.getField("name").getValueAsString());
            if (siSandbox.hasWorkingFile((Item)wi.getField("wfdelta").getValue())) {
               scmFileList.add(new ScmFile(memberFile.getAbsolutePath(), ScmFileStatus.UPDATED));
            } else {
               scmFileList.add(new ScmFile(memberFile.getAbsolutePath(), ScmFileStatus.DELETED));
            }
         }

         if (scmFileList.size() == 0) {
            this.getLogger().info("No local changes found!");
         }

         result = new StatusScmResult(scmFileList, new ScmResult("si viewsandbox", "", "", true));
      } catch (APIException var14) {
         ExceptionHandler eh = new ExceptionHandler(var14);
         this.getLogger().error("MKS API Exception: " + eh.getMessage());
         this.getLogger().debug(eh.getCommand() + " exited with return code " + eh.getExitCode());
         result = new StatusScmResult(eh.getCommand(), eh.getMessage(), "Exit Code: " + eh.getExitCode(), false);
      }

      return result;
   }
}

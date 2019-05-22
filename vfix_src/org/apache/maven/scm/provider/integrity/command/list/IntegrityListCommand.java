package org.apache.maven.scm.provider.integrity.command.list;

import com.mks.api.response.APIException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.list.AbstractListCommand;
import org.apache.maven.scm.command.list.ListScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.ExceptionHandler;
import org.apache.maven.scm.provider.integrity.Member;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;

public class IntegrityListCommand extends AbstractListCommand {
   public ListScmResult executeListCommand(ScmProviderRepository repository, ScmFileSet fileSet, boolean recursive, ScmVersion scmVersion) throws ScmException {
      IntegrityScmProviderRepository iRepo = (IntegrityScmProviderRepository)repository;
      this.getLogger().info("Listing all files in project " + iRepo.getConfigruationPath());

      ListScmResult result;
      try {
         List<Member> projectMembers = iRepo.getProject().listFiles(fileSet.getBasedir().getAbsolutePath());
         List<ScmFile> scmFileList = new ArrayList();
         Iterator it = projectMembers.iterator();

         while(it.hasNext()) {
            Member siMember = (Member)it.next();
            scmFileList.add(new ScmFile(siMember.getTargetFilePath(), ScmFileStatus.UNKNOWN));
         }

         result = new ListScmResult(scmFileList, new ScmResult("si viewproject", "", "", true));
      } catch (APIException var11) {
         ExceptionHandler eh = new ExceptionHandler(var11);
         this.getLogger().error("MKS API Exception: " + eh.getMessage());
         this.getLogger().debug(eh.getCommand() + " exited with return code " + eh.getExitCode());
         result = new ListScmResult(eh.getCommand(), eh.getMessage(), "Exit Code: " + eh.getExitCode(), false);
      }

      return result;
   }
}

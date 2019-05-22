package org.apache.maven.scm.provider.integrity.command.mkdir;

import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.mkdir.AbstractMkdirCommand;
import org.apache.maven.scm.command.mkdir.MkdirScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.ExceptionHandler;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;

public class IntegrityMkdirCommand extends AbstractMkdirCommand {
   public MkdirScmResult executeMkdirCommand(ScmProviderRepository repository, ScmFileSet fileSet, String message, boolean createInLocal) throws ScmException {
      String dirPath = "";
      Iterator<File> fit = fileSet.getFileList().iterator();
      if (fit.hasNext()) {
         dirPath = ((File)fit.next()).getPath().replace('\\', '/');
      }

      if (null != dirPath && dirPath.length() != 0) {
         this.getLogger().info("Creating subprojects one per directory, as required for " + dirPath);
         IntegrityScmProviderRepository iRepo = (IntegrityScmProviderRepository)repository;

         MkdirScmResult result;
         try {
            Response res = iRepo.getSandbox().createSubproject(dirPath);
            String subProject = res.getWorkItems().next().getResult().getField("resultant").getItem().getDisplayId();
            List<ScmFile> createdDirs = new ArrayList();
            createdDirs.add(new ScmFile(subProject, ScmFileStatus.ADDED));
            int exitCode = res.getExitCode();
            boolean success = exitCode == 0;
            this.getLogger().info("Successfully created subproject " + subProject);
            result = new MkdirScmResult(createdDirs, new ScmResult(res.getCommandString(), "", "Exit Code: " + exitCode, success));
         } catch (APIException var14) {
            ExceptionHandler eh = new ExceptionHandler(var14);
            this.getLogger().error("MKS API Exception: " + eh.getMessage());
            this.getLogger().info(eh.getCommand() + " exited with return code " + eh.getExitCode());
            result = new MkdirScmResult(eh.getCommand(), eh.getMessage(), "Exit Code: " + eh.getExitCode(), false);
         }

         return result;
      } else {
         throw new ScmException("A relative directory path is required to execute this command!");
      }
   }
}

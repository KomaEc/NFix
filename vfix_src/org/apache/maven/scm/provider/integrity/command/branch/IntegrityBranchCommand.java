package org.apache.maven.scm.provider.integrity.command.branch;

import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import java.util.ArrayList;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.branch.AbstractBranchCommand;
import org.apache.maven.scm.command.branch.BranchScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.ExceptionHandler;
import org.apache.maven.scm.provider.integrity.Project;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;

public class IntegrityBranchCommand extends AbstractBranchCommand {
   public BranchScmResult executeBranchCommand(ScmProviderRepository repository, ScmFileSet fileSet, String branchName, String message) throws ScmException {
      IntegrityScmProviderRepository iRepo = (IntegrityScmProviderRepository)repository;
      Project siProject = iRepo.getProject();
      this.getLogger().info("Attempting to branch project " + siProject.getProjectName() + " using branch name '" + branchName + "'");

      BranchScmResult result;
      try {
         Project.validateTag(branchName);
         Response res = siProject.createDevPath(branchName);
         int exitCode = res.getExitCode();
         boolean success = exitCode == 0;
         ScmResult scmResult = new ScmResult(res.getCommandString(), "", "Exit Code: " + exitCode, success);
         result = new BranchScmResult(new ArrayList(), scmResult);
      } catch (APIException var12) {
         ExceptionHandler eh = new ExceptionHandler(var12);
         this.getLogger().error("MKS API Exception: " + eh.getMessage());
         this.getLogger().info(eh.getCommand() + " exited with return code " + eh.getExitCode());
         result = new BranchScmResult(eh.getCommand(), eh.getMessage(), "Exit Code: " + eh.getExitCode(), false);
      } catch (Exception var13) {
         this.getLogger().error("Failed to checkpoint project! " + var13.getMessage());
         result = new BranchScmResult("si createdevpath", var13.getMessage(), "", false);
      }

      return result;
   }
}

package org.apache.maven.scm.provider.integrity.command.unedit;

import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.unedit.AbstractUnEditCommand;
import org.apache.maven.scm.command.unedit.UnEditScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.ExceptionHandler;
import org.apache.maven.scm.provider.integrity.Sandbox;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;

public class IntegrityUnEditCommand extends AbstractUnEditCommand {
   public UnEditScmResult executeUnEditCommand(ScmProviderRepository repository, ScmFileSet fileSet) throws ScmException {
      this.getLogger().info("Attempting to revert members in sandbox " + fileSet.getBasedir().getAbsolutePath());
      IntegrityScmProviderRepository iRepo = (IntegrityScmProviderRepository)repository;

      UnEditScmResult result;
      try {
         Sandbox siSandbox = iRepo.getSandbox();
         Response res = siSandbox.revertMembers();
         int exitCode = res.getExitCode();
         boolean success = exitCode == 0;
         result = new UnEditScmResult(res.getCommandString(), "", "Exit Code: " + exitCode, success);
      } catch (APIException var9) {
         ExceptionHandler eh = new ExceptionHandler(var9);
         this.getLogger().error("MKS API Exception: " + eh.getMessage());
         this.getLogger().info(eh.getCommand() + " exited with return code " + eh.getExitCode());
         result = new UnEditScmResult(eh.getCommand(), eh.getMessage(), "Exit Code: " + eh.getExitCode(), false);
      }

      return result;
   }
}

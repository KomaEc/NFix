package org.apache.maven.scm.provider.integrity.command.remove;

import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.remove.AbstractRemoveCommand;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.ExceptionHandler;
import org.apache.maven.scm.provider.integrity.Sandbox;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;

public class IntegrityRemoveCommand extends AbstractRemoveCommand {
   public RemoveScmResult executeRemoveCommand(ScmProviderRepository repository, ScmFileSet fileSet, String message) throws ScmException {
      this.getLogger().info("Attempting to un-register sandbox in directory " + fileSet.getBasedir().getAbsolutePath());
      IntegrityScmProviderRepository iRepo = (IntegrityScmProviderRepository)repository;

      RemoveScmResult result;
      try {
         Sandbox siSandbox = iRepo.getSandbox();
         Response res = siSandbox.drop();
         int exitCode = res.getExitCode();
         boolean success = exitCode == 0;
         result = new RemoveScmResult(res.getCommandString(), "", "Exit Code: " + exitCode, success);
      } catch (APIException var10) {
         ExceptionHandler eh = new ExceptionHandler(var10);
         this.getLogger().error("MKS API Exception: " + eh.getMessage());
         this.getLogger().info(eh.getCommand() + " exited with return code " + eh.getExitCode());
         result = new RemoveScmResult(eh.getCommand(), eh.getMessage(), "Exit Code: " + eh.getExitCode(), false);
      }

      return result;
   }
}

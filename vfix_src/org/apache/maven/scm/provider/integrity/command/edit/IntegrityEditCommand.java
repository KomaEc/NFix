package org.apache.maven.scm.provider.integrity.command.edit;

import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.edit.AbstractEditCommand;
import org.apache.maven.scm.command.edit.EditScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.ExceptionHandler;
import org.apache.maven.scm.provider.integrity.Sandbox;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;

public class IntegrityEditCommand extends AbstractEditCommand {
   public EditScmResult executeEditCommand(ScmProviderRepository repository, ScmFileSet fileSet) throws ScmException {
      this.getLogger().info("Attempting make files writeable in Sandbox " + fileSet.getBasedir().getAbsolutePath());
      IntegrityScmProviderRepository iRepo = (IntegrityScmProviderRepository)repository;

      EditScmResult result;
      try {
         Sandbox siSandbox = iRepo.getSandbox();
         Response res = siSandbox.makeWriteable();
         int exitCode = res.getExitCode();
         boolean success = exitCode == 0;
         result = new EditScmResult(res.getCommandString(), "", "Exit Code: " + exitCode, success);
      } catch (APIException var9) {
         ExceptionHandler eh = new ExceptionHandler(var9);
         this.getLogger().error("MKS API Exception: " + eh.getMessage());
         this.getLogger().info(eh.getCommand() + " exited with return code " + eh.getExitCode());
         result = new EditScmResult(eh.getCommand(), eh.getMessage(), "Exit Code: " + eh.getExitCode(), false);
      }

      return result;
   }
}

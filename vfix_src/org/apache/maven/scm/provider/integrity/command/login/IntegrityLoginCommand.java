package org.apache.maven.scm.provider.integrity.command.login;

import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.login.AbstractLoginCommand;
import org.apache.maven.scm.command.login.LoginScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.APISession;
import org.apache.maven.scm.provider.integrity.ExceptionHandler;
import org.apache.maven.scm.provider.integrity.Project;
import org.apache.maven.scm.provider.integrity.Sandbox;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;

public class IntegrityLoginCommand extends AbstractLoginCommand {
   public LoginScmResult executeLoginCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters params) throws ScmException {
      this.getLogger().info("Attempting to connect with the MKS Integrity Server");
      IntegrityScmProviderRepository iRepo = (IntegrityScmProviderRepository)repository;
      APISession api = iRepo.getAPISession();

      LoginScmResult result;
      try {
         Response res = api.connect(iRepo.getHost(), iRepo.getPort(), iRepo.getUser(), iRepo.getPassword());
         int exitCode = res.getExitCode();
         boolean success = exitCode == 0;
         result = new LoginScmResult(res.getCommandString(), "", "Exit Code: " + exitCode, success);
         Project siProject = new Project(api, iRepo.getConfigruationPath());
         Sandbox siSandbox = new Sandbox(api, siProject, fileSet.getBasedir().getAbsolutePath());
         iRepo.setProject(siProject);
         iRepo.setSandbox(siSandbox);
      } catch (APIException var12) {
         ExceptionHandler eh = new ExceptionHandler(var12);
         this.getLogger().error("MKS API Exception: " + eh.getMessage());
         this.getLogger().info(eh.getCommand() + " exited with return code " + eh.getExitCode());
         result = new LoginScmResult(eh.getCommand(), eh.getMessage(), "Exit Code: " + eh.getExitCode(), false);
      }

      return result;
   }
}

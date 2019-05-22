package org.apache.maven.scm.provider.integrity.command.lock;

import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import java.io.File;
import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.lock.AbstractLockCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.ExceptionHandler;
import org.apache.maven.scm.provider.integrity.Sandbox;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;

public class IntegrityLockCommand extends AbstractLockCommand {
   public ScmResult executeLockCommand(ScmProviderRepository repository, File workingDirectory, String filename) throws ScmException {
      this.getLogger().info("Attempting to lock file: " + filename);
      if (null != filename && filename.length() != 0) {
         IntegrityScmProviderRepository iRepo = (IntegrityScmProviderRepository)repository;

         ScmResult result;
         try {
            Sandbox siSandbox = iRepo.getSandbox();
            File memberFile = new File(workingDirectory.getAbsoluteFile() + File.separator + filename);
            Response res = siSandbox.lock(memberFile, filename);
            int exitCode = res.getExitCode();
            boolean success = exitCode == 0;
            result = new ScmResult(res.getCommandString(), "", "Exit Code: " + exitCode, success);
         } catch (APIException var11) {
            ExceptionHandler eh = new ExceptionHandler(var11);
            this.getLogger().error("MKS API Exception: " + eh.getMessage());
            this.getLogger().info(eh.getCommand() + " exited with return code " + eh.getExitCode());
            result = new ScmResult(eh.getCommand(), eh.getMessage(), "Exit Code: " + eh.getExitCode(), false);
         }

         return result;
      } else {
         throw new ScmException("A single filename is required to execute the lock command!");
      }
   }

   protected ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return this.executeLockCommand(repository, fileSet.getBasedir(), parameters.getString(CommandParameter.FILE));
   }
}

package org.apache.maven.scm.provider.integrity.command.unlock;

import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import java.io.File;
import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.unlock.AbstractUnlockCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.ExceptionHandler;
import org.apache.maven.scm.provider.integrity.Sandbox;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;

public class IntegrityUnlockCommand extends AbstractUnlockCommand {
   private String filename;

   public IntegrityUnlockCommand(String filename) {
      this.filename = filename;
   }

   public ScmResult executeUnlockCommand(ScmProviderRepository repository, File workingDirectory) throws ScmException {
      this.getLogger().info("Attempting to unlock file: " + this.filename);
      if (null != this.filename && this.filename.length() != 0) {
         IntegrityScmProviderRepository iRepo = (IntegrityScmProviderRepository)repository;

         ScmResult result;
         try {
            Sandbox siSandbox = iRepo.getSandbox();
            File memberFile = new File(workingDirectory.getAbsoluteFile() + File.separator + this.filename);
            Response res = siSandbox.unlock(memberFile, this.filename);
            int exitCode = res.getExitCode();
            boolean success = exitCode == 0;
            result = new ScmResult(res.getCommandString(), "", "Exit Code: " + exitCode, success);
         } catch (APIException var10) {
            ExceptionHandler eh = new ExceptionHandler(var10);
            this.getLogger().error("MKS API Exception: " + eh.getMessage());
            this.getLogger().info(eh.getCommand() + " exited with return code " + eh.getExitCode());
            result = new ScmResult(eh.getCommand(), eh.getMessage(), "Exit Code: " + eh.getExitCode(), false);
         }

         return result;
      } else {
         throw new ScmException("A single filename is required to execute the unlock command!");
      }
   }

   protected ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      this.filename = parameters.getString(CommandParameter.FILE);
      return this.executeUnlockCommand(repository, fileSet.getBasedir());
   }
}

package org.apache.maven.scm.provider.integrity.command.checkout;

import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import com.mks.api.response.Result;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkout.AbstractCheckOutCommand;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.ExceptionHandler;
import org.apache.maven.scm.provider.integrity.Sandbox;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;

public class IntegrityCheckOutCommand extends AbstractCheckOutCommand {
   public CheckOutScmResult executeCheckOutCommand(ScmProviderRepository repository, ScmFileSet fileSet, ScmVersion scmVersion, boolean recursive) throws ScmException {
      IntegrityScmProviderRepository iRepo = (IntegrityScmProviderRepository)repository;

      CheckOutScmResult result;
      try {
         this.getLogger().info("Attempting to checkout source for project " + iRepo.getProject().getConfigurationPath());
         String checkoutDir = System.getProperty("checkoutDirectory");
         Sandbox siSandbox;
         if (null != checkoutDir && checkoutDir.length() > 0) {
            siSandbox = new Sandbox(iRepo.getAPISession(), iRepo.getProject(), checkoutDir);
            iRepo.setSandbox(siSandbox);
         } else {
            siSandbox = iRepo.getSandbox();
         }

         this.getLogger().info("Sandbox location is " + siSandbox.getSandboxDir());
         if (siSandbox.create()) {
            Response res = siSandbox.resync();
            WorkItemIterator wit = res.getWorkItems();

            while(wit.hasNext()) {
               WorkItem wi = wit.next();
               if (wi.getModelType().equals("si.Member")) {
                  Result message = wi.getResult();
                  this.getLogger().debug(wi.getDisplayId() + " " + (null != message ? message.getMessage() : ""));
               }
            }

            int exitCode = res.getExitCode();
            boolean success = exitCode == 0;
            result = new CheckOutScmResult(res.getCommandString(), "", "Exit Code: " + exitCode, success);
         } else {
            result = new CheckOutScmResult("si createsandbox", "Failed to create sandbox!", "", false);
         }
      } catch (APIException var13) {
         ExceptionHandler eh = new ExceptionHandler(var13);
         this.getLogger().error("MKS API Exception: " + eh.getMessage());
         this.getLogger().info(eh.getCommand() + " exited with return code " + eh.getExitCode());
         result = new CheckOutScmResult(eh.getCommand(), eh.getMessage(), "Exit Code: " + eh.getExitCode(), false);
      }

      return result;
   }
}

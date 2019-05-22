package org.apache.maven.scm.provider.integrity.command.tag;

import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmTagParameters;
import org.apache.maven.scm.command.tag.AbstractTagCommand;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.ExceptionHandler;
import org.apache.maven.scm.provider.integrity.Project;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;

public class IntegrityTagCommand extends AbstractTagCommand {
   public TagScmResult executeTagCommand(ScmProviderRepository repository, ScmFileSet fileSet, String tagName, ScmTagParameters scmTagParameters) throws ScmException {
      this.getLogger().info("Attempting to checkpoint project associated with sandbox " + fileSet.getBasedir().getAbsolutePath());
      String message = scmTagParameters.getMessage();
      IntegrityScmProviderRepository iRepo = (IntegrityScmProviderRepository)repository;

      TagScmResult result;
      try {
         String chkptLabel = this.evalGroovyExpression(tagName);
         Project.validateTag(chkptLabel);
         String msg = null != message && message.length() != 0 ? message : System.getProperty("message");
         Project siProject = iRepo.getProject();
         if (!siProject.isBuild()) {
            Response res = siProject.checkpoint(msg, chkptLabel);
            int exitCode = res.getExitCode();
            boolean success = exitCode == 0;
            WorkItem wi = res.getWorkItem(siProject.getConfigurationPath());
            String chkpt = wi.getResult().getField("resultant").getItem().getId();
            this.getLogger().info("Successfully checkpointed project " + siProject.getConfigurationPath() + " with label '" + chkptLabel + "', new revision is " + chkpt);
            result = new TagScmResult(res.getCommandString(), wi.getResult().getMessage(), "Exit Code: " + exitCode, success);
         } else {
            this.getLogger().error("Cannot checkpoint a build project configuration: " + siProject.getConfigurationPath() + "!");
            result = new TagScmResult("si checkpoint", "Cannot checkpoint a build project configuration!", "", false);
         }
      } catch (CompilationFailedException var16) {
         this.getLogger().error("Groovy Compilation Exception: " + var16.getMessage());
         result = new TagScmResult("si checkpoint", var16.getMessage(), "", false);
      } catch (APIException var17) {
         ExceptionHandler eh = new ExceptionHandler(var17);
         this.getLogger().error("MKS API Exception: " + eh.getMessage());
         this.getLogger().info(eh.getCommand() + " exited with return code " + eh.getExitCode());
         result = new TagScmResult(eh.getCommand(), eh.getMessage(), "Exit Code: " + eh.getExitCode(), false);
      } catch (Exception var18) {
         this.getLogger().error("Failed to checkpoint project! " + var18.getMessage());
         result = new TagScmResult("si checkpoint", var18.getMessage(), "", false);
      }

      return result;
   }

   public String evalGroovyExpression(String expression) {
      Binding binding = new Binding();
      binding.setVariable("env", System.getenv());
      binding.setVariable("sys", System.getProperties());
      CompilerConfiguration config = new CompilerConfiguration();
      GroovyShell shell = new GroovyShell(binding, config);
      Object result = shell.evaluate("return \"" + expression + "\"");
      return result == null ? "" : result.toString().trim();
   }
}

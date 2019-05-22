package org.apache.maven.scm.command.branch;

import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmBranchParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;

public abstract class AbstractBranchCommand extends AbstractCommand {
   protected abstract ScmResult executeBranchCommand(ScmProviderRepository var1, ScmFileSet var2, String var3, String var4) throws ScmException;

   protected ScmResult executeBranchCommand(ScmProviderRepository repository, ScmFileSet fileSet, String branchName, ScmBranchParameters scmBranchParameters) throws ScmException {
      return this.executeBranchCommand(repository, fileSet, branchName, scmBranchParameters.getMessage());
   }

   public ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      String branchName = parameters.getString(CommandParameter.BRANCH_NAME);
      ScmBranchParameters scmBranchParameters = parameters.getScmBranchParameters(CommandParameter.SCM_BRANCH_PARAMETERS);
      String message = parameters.getString(CommandParameter.MESSAGE, "[maven-scm] copy for branch " + branchName);
      if (StringUtils.isBlank(scmBranchParameters.getMessage()) && StringUtils.isNotBlank(message)) {
         scmBranchParameters.setMessage(message);
      }

      return this.executeBranchCommand(repository, fileSet, branchName, scmBranchParameters);
   }
}

package org.apache.maven.scm.provider.jazz.command.branch;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.branch.AbstractBranchCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;

public class JazzBranchCommand extends AbstractBranchCommand {
   protected ScmResult executeBranchCommand(ScmProviderRepository repo, ScmFileSet fileSet, String branch, String message) throws ScmException {
      throw new ScmException("This provider does not support the branch command.");
   }
}

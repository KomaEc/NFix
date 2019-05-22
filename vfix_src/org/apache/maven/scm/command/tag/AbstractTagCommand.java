package org.apache.maven.scm.command.tag;

import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmTagParameters;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;

public abstract class AbstractTagCommand extends AbstractCommand {
   /** @deprecated */
   protected ScmResult executeTagCommand(ScmProviderRepository repository, ScmFileSet fileSet, String tagName, String message) throws ScmException {
      return this.executeTagCommand(repository, fileSet, tagName, new ScmTagParameters(message));
   }

   protected abstract ScmResult executeTagCommand(ScmProviderRepository var1, ScmFileSet var2, String var3, ScmTagParameters var4) throws ScmException;

   public ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      String tagName = parameters.getString(CommandParameter.TAG_NAME);
      ScmTagParameters scmTagParameters = parameters.getScmTagParameters(CommandParameter.SCM_TAG_PARAMETERS);
      String message = parameters.getString(CommandParameter.MESSAGE, (String)null);
      if (message != null) {
         scmTagParameters.setMessage(message);
      }

      if (scmTagParameters.getMessage() == null) {
         scmTagParameters.setMessage("[maven-scm] copy for tag " + tagName);
      }

      return this.executeTagCommand(repository, fileSet, tagName, scmTagParameters);
   }
}

package org.apache.maven.scm.command.checkin;

import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;

public abstract class AbstractCheckInCommand extends AbstractCommand {
   public static final String NAME = "check-in";

   protected abstract CheckInScmResult executeCheckInCommand(ScmProviderRepository var1, ScmFileSet var2, String var3, ScmVersion var4) throws ScmException;

   public ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      String message = parameters.getString(CommandParameter.MESSAGE);
      ScmVersion scmVersion = parameters.getScmVersion(CommandParameter.SCM_VERSION, (ScmVersion)null);
      return this.executeCheckInCommand(repository, fileSet, message, scmVersion);
   }
}

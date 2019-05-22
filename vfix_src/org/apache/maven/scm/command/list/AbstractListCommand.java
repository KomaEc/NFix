package org.apache.maven.scm.command.list;

import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;

public abstract class AbstractListCommand extends AbstractCommand {
   protected abstract ListScmResult executeListCommand(ScmProviderRepository var1, ScmFileSet var2, boolean var3, ScmVersion var4) throws ScmException;

   public ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      if (fileSet.getFileList().isEmpty()) {
         throw new IllegalArgumentException("fileSet can not be empty");
      } else {
         boolean recursive = parameters.getBoolean(CommandParameter.RECURSIVE);
         ScmVersion scmVersion = parameters.getScmVersion(CommandParameter.SCM_VERSION, (ScmVersion)null);
         return this.executeListCommand(repository, fileSet, recursive, scmVersion);
      }
   }
}

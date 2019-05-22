package org.apache.maven.scm.command.mkdir;

import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;

public abstract class AbstractMkdirCommand extends AbstractCommand {
   protected abstract MkdirScmResult executeMkdirCommand(ScmProviderRepository var1, ScmFileSet var2, String var3, boolean var4) throws ScmException;

   protected ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      if (fileSet.getFileList().isEmpty()) {
         throw new IllegalArgumentException("fileSet can not be empty");
      } else {
         return this.executeMkdirCommand(repository, fileSet, parameters.getString(CommandParameter.MESSAGE), parameters.getBoolean(CommandParameter.SCM_MKDIR_CREATE_IN_LOCAL));
      }
   }
}

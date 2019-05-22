package org.apache.maven.scm.command.export;

import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;

public abstract class AbstractExportCommand extends AbstractCommand {
   protected abstract ExportScmResult executeExportCommand(ScmProviderRepository var1, ScmFileSet var2, ScmVersion var3, String var4) throws ScmException;

   protected ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      ScmVersion scmVersion = parameters.getScmVersion(CommandParameter.SCM_VERSION, (ScmVersion)null);
      String outputDirectory = parameters.getString(CommandParameter.OUTPUT_DIRECTORY, (String)null);
      return this.executeExportCommand(repository, fileSet, scmVersion, outputDirectory);
   }
}

package org.apache.maven.scm.provider.accurev.command;

import java.io.File;
import java.util.List;
import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.accurev.AccuRevException;
import org.apache.maven.scm.provider.accurev.AccuRevScmProviderRepository;
import org.apache.maven.scm.provider.accurev.AccuRevVersion;

public abstract class AbstractAccuRevExtractSourceCommand extends AbstractAccuRevCommand {
   public AbstractAccuRevExtractSourceCommand(ScmLogger logger) {
      super(logger);
   }

   protected ScmResult executeAccurevCommand(AccuRevScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException, AccuRevException {
      ScmVersion scmVersion = parameters.getScmVersion(CommandParameter.SCM_VERSION, (ScmVersion)null);
      File basedir = fileSet.getBasedir();
      String outputDirectory = parameters.getString(CommandParameter.OUTPUT_DIRECTORY, (String)null);
      if (outputDirectory != null) {
         basedir = new File(outputDirectory);
      }

      if (!basedir.exists()) {
         basedir.mkdirs();
      }

      if (basedir.isDirectory() && basedir.list().length == 0) {
         AccuRevVersion accuRevVersion = repository.getAccuRevVersion(scmVersion);
         List<File> checkedOutFiles = this.extractSource(repository, basedir, accuRevVersion);
         List<ScmFile> scmFiles = checkedOutFiles == null ? null : getScmFiles(checkedOutFiles, ScmFileStatus.CHECKED_OUT);
         return this.getScmResult(repository, scmFiles, scmVersion);
      } else {
         throw new ScmException("Checkout directory " + basedir.getAbsolutePath() + " not empty");
      }
   }

   protected abstract ScmResult getScmResult(AccuRevScmProviderRepository var1, List<ScmFile> var2, ScmVersion var3);

   protected abstract List<File> extractSource(AccuRevScmProviderRepository var1, File var2, AccuRevVersion var3) throws AccuRevException;
}

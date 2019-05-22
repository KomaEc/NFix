package org.apache.maven.scm.provider.accurev.command.add;

import java.io.File;
import java.util.List;
import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.accurev.AccuRev;
import org.apache.maven.scm.provider.accurev.AccuRevException;
import org.apache.maven.scm.provider.accurev.AccuRevScmProviderRepository;
import org.apache.maven.scm.provider.accurev.command.AbstractAccuRevCommand;

public class AccuRevAddCommand extends AbstractAccuRevCommand {
   public AccuRevAddCommand(ScmLogger logger) {
      super(logger);
   }

   protected ScmResult executeAccurevCommand(AccuRevScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException, AccuRevException {
      AccuRev accuRev = repository.getAccuRev();
      String message = parameters.getString(CommandParameter.MESSAGE, "");
      File basedir = fileSet.getBasedir();
      List<File> relativeFiles = fileSet.getFileList();
      List<File> addedFiles = accuRev.add(basedir, relativeFiles, message);
      if (addedFiles != null) {
         List<ScmFile> resultFiles = getScmFiles(addedFiles, ScmFileStatus.ADDED);
         return new AddScmResult(accuRev.getCommandLines(), resultFiles);
      } else {
         return new AddScmResult(accuRev.getCommandLines(), "AccuRev Error", accuRev.getErrorOutput(), false);
      }
   }

   public AddScmResult add(ScmProviderRepository repo, ScmFileSet scmFileSet, CommandParameters commandParameters) throws ScmException {
      return (AddScmResult)this.execute(repo, scmFileSet, commandParameters);
   }
}

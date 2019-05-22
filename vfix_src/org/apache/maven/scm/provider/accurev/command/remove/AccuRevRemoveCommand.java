package org.apache.maven.scm.provider.accurev.command.remove;

import java.io.File;
import java.util.List;
import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.accurev.AccuRev;
import org.apache.maven.scm.provider.accurev.AccuRevException;
import org.apache.maven.scm.provider.accurev.AccuRevScmProviderRepository;
import org.apache.maven.scm.provider.accurev.command.AbstractAccuRevCommand;

public class AccuRevRemoveCommand extends AbstractAccuRevCommand {
   public AccuRevRemoveCommand(ScmLogger logger) {
      super(logger);
   }

   protected ScmResult executeAccurevCommand(AccuRevScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException, AccuRevException {
      AccuRev accuRev = repository.getAccuRev();
      String message = parameters.getString(CommandParameter.MESSAGE, "");
      File basedir = fileSet.getBasedir();
      List<File> relativeFiles = fileSet.getFileList();
      List<File> removedFiles = accuRev.defunct(basedir, relativeFiles, message);
      if (removedFiles != null) {
         List<ScmFile> resultFiles = getScmFiles(removedFiles, ScmFileStatus.DELETED);
         return new RemoveScmResult(accuRev.getCommandLines(), resultFiles);
      } else {
         return new RemoveScmResult(accuRev.getCommandLines(), "AccuRev Error", accuRev.getErrorOutput(), false);
      }
   }

   public RemoveScmResult remove(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (RemoveScmResult)this.execute(repository, fileSet, parameters);
   }
}

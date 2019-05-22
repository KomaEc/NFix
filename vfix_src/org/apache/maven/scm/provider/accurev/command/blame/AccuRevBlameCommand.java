package org.apache.maven.scm.provider.accurev.command.blame;

import java.io.File;
import java.util.List;
import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.blame.BlameLine;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.accurev.AccuRev;
import org.apache.maven.scm.provider.accurev.AccuRevException;
import org.apache.maven.scm.provider.accurev.AccuRevScmProviderRepository;
import org.apache.maven.scm.provider.accurev.command.AbstractAccuRevCommand;

public class AccuRevBlameCommand extends AbstractAccuRevCommand {
   public AccuRevBlameCommand(ScmLogger logger) {
      super(logger);
   }

   protected BlameScmResult executeAccurevCommand(AccuRevScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException, AccuRevException {
      AccuRev accuRev = repository.getAccuRev();
      File file = new File(parameters.getString(CommandParameter.FILE));
      List<BlameLine> lines = accuRev.annotate(fileSet.getBasedir(), file);
      return lines != null ? new BlameScmResult(accuRev.getCommandLines(), lines) : new BlameScmResult(accuRev.getCommandLines(), "AccuRev Error", accuRev.getErrorOutput(), false);
   }

   public BlameScmResult blame(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (BlameScmResult)this.execute(repository, fileSet, parameters);
   }
}

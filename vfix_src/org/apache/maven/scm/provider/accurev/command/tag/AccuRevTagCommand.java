package org.apache.maven.scm.provider.accurev.command.tag;

import java.io.File;
import java.util.List;
import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.accurev.AccuRev;
import org.apache.maven.scm.provider.accurev.AccuRevException;
import org.apache.maven.scm.provider.accurev.AccuRevInfo;
import org.apache.maven.scm.provider.accurev.AccuRevScmProviderRepository;
import org.apache.maven.scm.provider.accurev.command.AbstractAccuRevCommand;

public class AccuRevTagCommand extends AbstractAccuRevCommand {
   public AccuRevTagCommand(ScmLogger logger) {
      super(logger);
   }

   protected ScmResult executeAccurevCommand(AccuRevScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException, AccuRevException {
      AccuRev accuRev = repository.getAccuRev();
      String snapshotName = parameters.getString(CommandParameter.TAG_NAME);
      snapshotName = repository.getSnapshotName(snapshotName);
      File basedir = fileSet.getBasedir();
      boolean success = true;
      AccuRevInfo info = accuRev.info(basedir);
      List<File> taggedFiles = null;
      success = accuRev.mksnap(snapshotName, info.getBasis());
      if (success) {
         taggedFiles = accuRev.statTag(snapshotName);
      }

      return success && taggedFiles != null ? new TagScmResult(accuRev.getCommandLines(), getScmFiles(taggedFiles, ScmFileStatus.TAGGED)) : new TagScmResult(accuRev.getCommandLines(), "AccuRev error", accuRev.getErrorOutput(), false);
   }

   public TagScmResult tag(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (TagScmResult)this.execute(repository, fileSet, parameters);
   }
}

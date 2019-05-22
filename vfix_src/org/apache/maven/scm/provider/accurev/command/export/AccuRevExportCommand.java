package org.apache.maven.scm.provider.accurev.command.export;

import java.io.File;
import java.util.Collections;
import java.util.List;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.export.ExportScmResult;
import org.apache.maven.scm.command.export.ExportScmResultWithRevision;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.accurev.AccuRev;
import org.apache.maven.scm.provider.accurev.AccuRevCapability;
import org.apache.maven.scm.provider.accurev.AccuRevException;
import org.apache.maven.scm.provider.accurev.AccuRevInfo;
import org.apache.maven.scm.provider.accurev.AccuRevScmProviderRepository;
import org.apache.maven.scm.provider.accurev.AccuRevVersion;
import org.apache.maven.scm.provider.accurev.command.AbstractAccuRevExtractSourceCommand;

public class AccuRevExportCommand extends AbstractAccuRevExtractSourceCommand {
   public AccuRevExportCommand(ScmLogger logger) {
      super(logger);
   }

   public ExportScmResult export(ScmProviderRepository repository, ScmFileSet scmFileSet, CommandParameters params) throws ScmException {
      return (ExportScmResult)this.execute(repository, scmFileSet, params);
   }

   protected List<File> extractSource(AccuRevScmProviderRepository repository, File basedir, AccuRevVersion version) throws AccuRevException {
      AccuRev accuRev = repository.getAccuRev();
      AccuRevInfo info = accuRev.info(basedir);
      String basisStream = version.getBasisStream();
      String transactionId = version.getTimeSpec();
      if (!AccuRevVersion.isNow(transactionId) && !AccuRevCapability.POPULATE_TO_TRANSACTION.isSupported(accuRev.getClientVersion())) {
         this.getLogger().warn(String.format("Ignoring transaction id %s, Export can only extract current sources", transactionId));
         transactionId = "now";
      } else {
         accuRev.syncReplica();
      }

      boolean removedWorkspace = false;
      if (info.isWorkSpace()) {
         String stat = accuRev.stat(basedir);
         if (stat != null) {
            throw new AccuRevException(String.format("Cannot populate %s, as it is a non-ignored subdirectory of workspace %s rooted at %s.", basedir.getAbsolutePath(), info.getWorkSpace(), info.getTop()));
         }

         removedWorkspace = accuRev.rmws(info.getWorkSpace());
      }

      List var10;
      try {
         File path = new File(repository.getDepotRelativeProjectPath());
         var10 = accuRev.popExternal(basedir, basisStream, transactionId, Collections.singletonList(path));
      } finally {
         if (removedWorkspace) {
            accuRev.reactivate(info.getWorkSpace());
         }

      }

      return var10;
   }

   protected ScmResult getScmResult(AccuRevScmProviderRepository repository, List<ScmFile> scmFiles, ScmVersion scmVersion) {
      AccuRev accuRev = repository.getAccuRev();
      if (scmFiles != null) {
         return (ScmResult)(scmVersion == null ? new ExportScmResult(accuRev.getCommandLines(), scmFiles) : new ExportScmResultWithRevision(accuRev.getCommandLines(), scmFiles, scmVersion.toString()));
      } else {
         return new ExportScmResult(accuRev.getCommandLines(), "AccuRev Error", accuRev.getErrorOutput(), false);
      }
   }
}

package org.apache.maven.scm.provider.accurev.command.status;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.accurev.AccuRev;
import org.apache.maven.scm.provider.accurev.AccuRevCapability;
import org.apache.maven.scm.provider.accurev.AccuRevException;
import org.apache.maven.scm.provider.accurev.AccuRevScmProviderRepository;
import org.apache.maven.scm.provider.accurev.AccuRevStat;
import org.apache.maven.scm.provider.accurev.CategorisedElements;
import org.apache.maven.scm.provider.accurev.command.AbstractAccuRevCommand;

public class AccuRevStatusCommand extends AbstractAccuRevCommand {
   public AccuRevStatusCommand(ScmLogger logger) {
      super(logger);
   }

   protected ScmResult executeAccurevCommand(AccuRevScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException, AccuRevException {
      AccuRev accuRev = repository.getAccuRev();
      File basedir = fileSet.getBasedir();
      List<File> elements = fileSet.getFileList();
      List<File> defunctElements = accuRev.stat(basedir, elements, AccuRevStat.DEFUNCT);
      if (defunctElements == null) {
         return this.error(accuRev, "Failed retrieving defunct elements");
      } else {
         List<File> keptElements = accuRev.stat(basedir, elements, AccuRevStat.KEPT);
         if (keptElements == null) {
            return this.error(accuRev, "Failed retrieving kept elements");
         } else {
            List<File> modOrAddedElements = new ArrayList();
            Iterator i$ = keptElements.iterator();

            while(i$.hasNext()) {
               File file = (File)i$.next();
               if (!defunctElements.contains(file)) {
                  modOrAddedElements.add(file);
               }
            }

            List<File> modifiedElements = accuRev.stat(basedir, elements, AccuRevStat.MODIFIED);
            if (modifiedElements == null) {
               return this.error(accuRev, "Failed retrieving modified elements");
            } else {
               modOrAddedElements.addAll(modifiedElements);
               CategorisedElements catElems = accuRev.statBackingStream(basedir, modOrAddedElements);
               if (catElems == null) {
                  return this.error(accuRev, "Failed stat backing stream to split modified and added elements");
               } else {
                  modifiedElements = catElems.getMemberElements();
                  Object addedElements;
                  if (AccuRevCapability.STAT_ADDED_NOT_PROMOTED_BUG.isSupported(accuRev.getClientVersion())) {
                     modOrAddedElements.removeAll(modifiedElements);
                     addedElements = modOrAddedElements;
                  } else {
                     addedElements = catElems.getNonMemberElements();
                  }

                  List<File> missingElements = accuRev.stat(basedir, elements, AccuRevStat.MISSING);
                  if (missingElements == null) {
                     return this.error(accuRev, "Failed retrieving missing elements");
                  } else {
                     List<File> externalElements = accuRev.stat(basedir, elements, AccuRevStat.EXTERNAL);
                     if (externalElements == null) {
                        return this.error(accuRev, "Failed retrieving external elements");
                     } else {
                        List<ScmFile> resultFiles = getScmFiles(defunctElements, ScmFileStatus.DELETED);
                        resultFiles.addAll(getScmFiles(modifiedElements, ScmFileStatus.MODIFIED));
                        resultFiles.addAll(getScmFiles((List)addedElements, ScmFileStatus.ADDED));
                        resultFiles.addAll(getScmFiles(missingElements, ScmFileStatus.MISSING));
                        resultFiles.addAll(getScmFiles(externalElements, ScmFileStatus.UNKNOWN));
                        return new StatusScmResult(accuRev.getCommandLines(), resultFiles);
                     }
                  }
               }
            }
         }
      }
   }

   private ScmResult error(AccuRev accuRev, String message) {
      return new StatusScmResult(accuRev.getCommandLines(), "AccuRev " + message, accuRev.getErrorOutput(), false);
   }

   public StatusScmResult status(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (StatusScmResult)this.execute(repository, fileSet, parameters);
   }
}

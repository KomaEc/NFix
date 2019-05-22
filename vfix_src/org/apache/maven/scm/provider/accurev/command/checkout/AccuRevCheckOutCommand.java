package org.apache.maven.scm.provider.accurev.command.checkout;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.accurev.AccuRev;
import org.apache.maven.scm.provider.accurev.AccuRevException;
import org.apache.maven.scm.provider.accurev.AccuRevInfo;
import org.apache.maven.scm.provider.accurev.AccuRevScmProviderRepository;
import org.apache.maven.scm.provider.accurev.AccuRevVersion;
import org.apache.maven.scm.provider.accurev.command.AbstractAccuRevExtractSourceCommand;

public class AccuRevCheckOutCommand extends AbstractAccuRevExtractSourceCommand {
   public AccuRevCheckOutCommand(ScmLogger logger) {
      super(logger);
   }

   public CheckOutScmResult checkout(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return (CheckOutScmResult)this.execute(repository, fileSet, parameters);
   }

   protected List<File> extractSource(AccuRevScmProviderRepository repository, File basedir, AccuRevVersion version) throws AccuRevException {
      AccuRev accuRev = repository.getAccuRev();
      AccuRevInfo info = accuRev.info(basedir);
      List<File> extractedFiles = new ArrayList();
      String basisStream = version.getBasisStream();
      String transactionId = version.getTimeSpec();
      boolean success = true;
      List poppedFiles;
      if (info.isWorkSpace()) {
         if (!repository.isWorkSpaceTop(info)) {
            throw new AccuRevException(String.format("Can't checkout to %s, a subdirectory of existing workspace %s", basedir, info.getWorkSpace()));
         }

         if (!basisStream.equals(info.getBasis())) {
            success = accuRev.chws(basedir, info.getWorkSpace(), basisStream);
         }

         if (success) {
            poppedFiles = accuRev.pop(basedir, (Collection)null);
            if (poppedFiles != null) {
               extractedFiles.addAll(poppedFiles);
            } else {
               success = false;
            }
         }
      } else {
         String workSpaceName = getWorkSpaceName(basedir, basisStream);
         success = accuRev.mkws(basisStream, workSpaceName, basedir);
         transactionId = "now";
         if (success) {
            this.getLogger().info("Created workspace " + workSpaceName);
         }
      }

      if (success) {
         poppedFiles = accuRev.update(basedir, transactionId);
         if (poppedFiles != null) {
            extractedFiles.addAll(poppedFiles);
         } else {
            success = false;
         }
      }

      return success ? extractedFiles : null;
   }

   protected ScmResult getScmResult(AccuRevScmProviderRepository repository, List<ScmFile> scmFiles, ScmVersion version) {
      AccuRev accuRev = repository.getAccuRev();
      return scmFiles != null ? new CheckOutScmResult(accuRev.getCommandLines(), scmFiles, repository.getProjectPath()) : new CheckOutScmResult(accuRev.getCommandLines(), "AccuRev Error", accuRev.getErrorOutput(), false);
   }

   public static String getWorkSpaceName(File basedir, String basisStream) {
      String baseName = basedir.getName();
      String workSpaceName;
      if (baseName.contains(basisStream)) {
         workSpaceName = baseName;
      } else if (basisStream.contains(baseName)) {
         workSpaceName = basisStream;
      } else {
         workSpaceName = basisStream + "_" + baseName;
      }

      return workSpaceName;
   }
}

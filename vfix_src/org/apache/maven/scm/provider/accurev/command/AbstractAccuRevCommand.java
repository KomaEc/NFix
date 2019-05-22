package org.apache.maven.scm.provider.accurev.command;

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
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.accurev.AccuRevException;
import org.apache.maven.scm.provider.accurev.AccuRevScmProviderRepository;

public abstract class AbstractAccuRevCommand extends AbstractCommand {
   public AbstractAccuRevCommand(ScmLogger logger) {
      this.setLogger(logger);
   }

   protected abstract ScmResult executeAccurevCommand(AccuRevScmProviderRepository var1, ScmFileSet var2, CommandParameters var3) throws ScmException, AccuRevException;

   protected final ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      if (!(repository instanceof AccuRevScmProviderRepository)) {
         throw new ScmException("Not an AccuRev repository " + repository);
      } else {
         AccuRevScmProviderRepository accuRevRepository = (AccuRevScmProviderRepository)repository;
         accuRevRepository.getAccuRev().reset();

         try {
            return this.executeAccurevCommand(accuRevRepository, fileSet, parameters);
         } catch (AccuRevException var6) {
            throw new ScmException("Error invoking AccuRev command", var6);
         }
      }
   }

   protected static List<ScmFile> getScmFiles(List<File> files, ScmFileStatus status) {
      ArrayList<ScmFile> resultFiles = new ArrayList(files.size());
      Iterator i$ = files.iterator();

      while(i$.hasNext()) {
         File addedFile = (File)i$.next();
         resultFiles.add(new ScmFile(addedFile.getPath(), status));
      }

      return resultFiles;
   }
}

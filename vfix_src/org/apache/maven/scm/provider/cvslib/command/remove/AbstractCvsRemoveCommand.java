package org.apache.maven.scm.provider.cvslib.command.remove;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.remove.AbstractRemoveCommand;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.cvslib.command.CvsCommand;
import org.apache.maven.scm.provider.cvslib.command.CvsCommandUtils;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;
import org.codehaus.plexus.util.cli.Commandline;

public abstract class AbstractCvsRemoveCommand extends AbstractRemoveCommand implements CvsCommand {
   protected ScmResult executeRemoveCommand(ScmProviderRepository repo, ScmFileSet fileSet, String message) throws ScmException {
      CvsScmProviderRepository repository = (CvsScmProviderRepository)repo;
      Commandline cl = CvsCommandUtils.getBaseCommand("remove", repository, fileSet);
      cl.createArg().setValue("-f");
      cl.createArg().setValue("-l");
      List<File> files = fileSet.getFileList();
      List<ScmFile> removedFiles = new ArrayList();
      Iterator i$ = files.iterator();

      while(i$.hasNext()) {
         File file = (File)i$.next();
         String path = file.getPath().replace('\\', '/');
         cl.createArg().setValue(path);
         removedFiles.add(new ScmFile(path, ScmFileStatus.DELETED));
      }

      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + cl);
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      return this.executeCvsCommand(cl, removedFiles);
   }

   protected abstract RemoveScmResult executeCvsCommand(Commandline var1, List<ScmFile> var2) throws ScmException;
}

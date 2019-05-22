package org.apache.maven.scm.provider.cvslib.command.add;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.add.AbstractAddCommand;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.cvslib.command.CvsCommand;
import org.apache.maven.scm.provider.cvslib.command.CvsCommandUtils;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;
import org.codehaus.plexus.util.cli.Commandline;

public abstract class AbstractCvsAddCommand extends AbstractAddCommand implements CvsCommand {
   protected ScmResult executeAddCommand(ScmProviderRepository repo, ScmFileSet fileSet, String message, boolean binary) throws ScmException {
      CvsScmProviderRepository repository = (CvsScmProviderRepository)repo;
      Commandline cl = CvsCommandUtils.getBaseCommand("add", repository, fileSet);
      if (binary) {
         cl.createArg().setValue("-kb");
      }

      if (message != null && message.length() > 0) {
         cl.createArg().setValue("-m");
         cl.createArg().setValue("\"" + message + "\"");
      }

      List<ScmFile> addedFiles = new ArrayList(fileSet.getFileList().size());
      Iterator i$ = fileSet.getFileList().iterator();

      while(i$.hasNext()) {
         File file = (File)i$.next();
         String path = file.getPath().replace('\\', '/');
         cl.createArg().setValue(path);
         addedFiles.add(new ScmFile(path, ScmFileStatus.ADDED));
      }

      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + cl);
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      return this.executeCvsCommand(cl, addedFiles);
   }

   protected abstract AddScmResult executeCvsCommand(Commandline var1, List<ScmFile> var2) throws ScmException;
}

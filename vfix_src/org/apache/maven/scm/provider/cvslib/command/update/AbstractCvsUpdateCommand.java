package org.apache.maven.scm.provider.cvslib.command.update;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.update.AbstractUpdateCommand;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.cvslib.command.CvsCommand;
import org.apache.maven.scm.provider.cvslib.command.CvsCommandUtils;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

public abstract class AbstractCvsUpdateCommand extends AbstractUpdateCommand implements CvsCommand {
   public UpdateScmResult executeUpdateCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion version) throws ScmException {
      CvsScmProviderRepository repository = (CvsScmProviderRepository)repo;
      Commandline cl = CvsCommandUtils.getBaseCommand("update", repository, fileSet, false);
      cl.createArg().setValue("-d");
      if (version != null && StringUtils.isNotEmpty(version.getName())) {
         cl.createArg().setValue("-r" + version.getName());
      }

      List<File> files = fileSet.getFileList();
      if (!files.isEmpty()) {
         Iterator fileIterator = files.iterator();

         while(fileIterator.hasNext()) {
            cl.createArg().setValue(((File)fileIterator.next()).getPath());
         }
      }

      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + cl);
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      return this.executeCvsCommand(cl);
   }

   protected abstract UpdateScmResult executeCvsCommand(Commandline var1) throws ScmException;
}

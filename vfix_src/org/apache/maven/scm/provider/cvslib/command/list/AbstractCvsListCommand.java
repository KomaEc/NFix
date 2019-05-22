package org.apache.maven.scm.provider.cvslib.command.list;

import java.io.File;
import java.util.Iterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.list.AbstractListCommand;
import org.apache.maven.scm.command.list.ListScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.cvslib.command.CvsCommand;
import org.apache.maven.scm.provider.cvslib.command.CvsCommandUtils;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

public abstract class AbstractCvsListCommand extends AbstractListCommand implements CvsCommand {
   protected ListScmResult executeListCommand(ScmProviderRepository repo, ScmFileSet fileSet, boolean recursive, ScmVersion version) throws ScmException {
      CvsScmProviderRepository repository = (CvsScmProviderRepository)repo;
      Commandline cl = CvsCommandUtils.getBaseCommand("rls", repository, fileSet, "-n");
      if (version != null && !StringUtils.isEmpty(version.getName())) {
         cl.createArg().setValue("-r");
         cl.createArg().setValue(version.getName());
      }

      cl.createArg().setValue("-d");
      cl.createArg().setValue("-e");
      if (recursive) {
         cl.createArg().setValue("-R");
      }

      String path;
      for(Iterator it = fileSet.getFileList().iterator(); it.hasNext(); cl.createArg().setValue(path)) {
         File target = (File)it.next();
         path = target.getPath();
         if (path.startsWith("\\")) {
            path = path.substring(1);
         }
      }

      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + cl);
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      return this.executeCvsCommand(cl);
   }

   protected abstract ListScmResult executeCvsCommand(Commandline var1) throws ScmException;
}

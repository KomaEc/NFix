package org.apache.maven.scm.provider.cvslib.command.tag;

import java.io.File;
import java.util.Iterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmTagParameters;
import org.apache.maven.scm.command.tag.AbstractTagCommand;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.cvslib.command.CvsCommand;
import org.apache.maven.scm.provider.cvslib.command.CvsCommandUtils;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;
import org.apache.maven.scm.provider.cvslib.util.CvsUtil;
import org.apache.maven.scm.providers.cvslib.settings.Settings;
import org.codehaus.plexus.util.cli.Commandline;

public abstract class AbstractCvsTagCommand extends AbstractTagCommand implements CvsCommand {
   public ScmResult executeTagCommand(ScmProviderRepository repo, ScmFileSet fileSet, String tag, String message) throws ScmException {
      return this.executeTagCommand(repo, fileSet, tag, new ScmTagParameters(message));
   }

   public ScmResult executeTagCommand(ScmProviderRepository repo, ScmFileSet fileSet, String tag, ScmTagParameters scmTagParameters) throws ScmException {
      CvsScmProviderRepository repository = (CvsScmProviderRepository)repo;
      Commandline cl = CvsCommandUtils.getBaseCommand("tag", repository, fileSet, false);
      Settings settings = CvsUtil.getSettings();
      if (settings.isUseForceTag()) {
         cl.createArg().setValue("-F");
      }

      cl.createArg().setValue("-c");
      cl.createArg().setValue(tag);
      if (fileSet.getFileList() != null && !fileSet.getFileList().isEmpty()) {
         Iterator it = fileSet.getFileList().iterator();

         while(it.hasNext()) {
            File fileName = (File)it.next();
            cl.createArg().setValue(fileName.toString());
         }
      }

      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + cl);
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      return this.executeCvsCommand(cl);
   }

   protected abstract TagScmResult executeCvsCommand(Commandline var1) throws ScmException;
}

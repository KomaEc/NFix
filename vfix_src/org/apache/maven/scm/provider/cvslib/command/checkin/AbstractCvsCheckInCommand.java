package org.apache.maven.scm.provider.cvslib.command.checkin;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkin.AbstractCheckInCommand;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.cvslib.command.CvsCommand;
import org.apache.maven.scm.provider.cvslib.command.CvsCommandUtils;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

public abstract class AbstractCvsCheckInCommand extends AbstractCheckInCommand implements CvsCommand {
   protected CheckInScmResult executeCheckInCommand(ScmProviderRepository repo, ScmFileSet fileSet, String message, ScmVersion version) throws ScmException {
      CvsScmProviderRepository repository = (CvsScmProviderRepository)repo;
      Commandline cl = CvsCommandUtils.getBaseCommand("commit", repository, fileSet, false);
      if (version != null && !StringUtils.isEmpty(version.getName())) {
         cl.createArg().setValue("-r" + version.getName());
      }

      cl.createArg().setValue("-R");
      cl.createArg().setValue("-F");

      File messageFile;
      try {
         messageFile = File.createTempFile("scm-commit-message", ".txt");
         FileUtils.fileWrite(messageFile.getAbsolutePath(), message);
      } catch (IOException var12) {
         throw new ScmException("Error while making a temporary commit message file.");
      }

      cl.createArg().setValue(messageFile.getAbsolutePath());
      List<File> files = fileSet.getFileList();
      Iterator i$ = files.iterator();

      while(i$.hasNext()) {
         File f = (File)i$.next();
         cl.createArg().setValue(f.getPath().replace('\\', '/'));
      }

      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Executing: " + cl);
         this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
      }

      CheckInScmResult result = this.executeCvsCommand(cl, repository, messageFile);

      try {
         FileUtils.forceDelete(messageFile);
      } catch (IOException var11) {
      }

      return result;
   }

   protected abstract CheckInScmResult executeCvsCommand(Commandline var1, CvsScmProviderRepository var2, File var3) throws ScmException;
}

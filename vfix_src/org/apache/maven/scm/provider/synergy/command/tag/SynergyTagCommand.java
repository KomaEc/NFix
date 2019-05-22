package org.apache.maven.scm.provider.synergy.command.tag;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmTagParameters;
import org.apache.maven.scm.command.tag.AbstractTagCommand;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.synergy.command.SynergyCommand;
import org.apache.maven.scm.provider.synergy.repository.SynergyScmProviderRepository;
import org.apache.maven.scm.provider.synergy.util.SynergyRole;
import org.apache.maven.scm.provider.synergy.util.SynergyUtil;

public class SynergyTagCommand extends AbstractTagCommand implements SynergyCommand {
   protected ScmResult executeTagCommand(ScmProviderRepository repository, ScmFileSet fileSet, String tag, String message) throws ScmException {
      return this.executeTagCommand(repository, fileSet, tag, new ScmTagParameters(message));
   }

   protected ScmResult executeTagCommand(ScmProviderRepository repository, ScmFileSet fileSet, String tag, ScmTagParameters scmTagParameters) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing tag command...");
      }

      SynergyScmProviderRepository repo = (SynergyScmProviderRepository)repository;
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("basedir: " + fileSet.getBasedir());
      }

      String ccmAddr = SynergyUtil.start(this.getLogger(), repo.getUser(), repo.getPassword(), SynergyRole.BUILD_MGR);

      try {
         SynergyUtil.reconfigureProperties(this.getLogger(), repo.getProjectSpec(), ccmAddr);
         SynergyUtil.reconfigure(this.getLogger(), repo.getProjectSpec(), ccmAddr);
         SynergyUtil.createBaseline(this.getLogger(), repo.getProjectSpec(), tag, repo.getProjectRelease(), repo.getProjectPurpose(), ccmAddr);
      } finally {
         SynergyUtil.stop(this.getLogger(), ccmAddr);
      }

      ArrayList files = new ArrayList(fileSet.getFileList().size());
      Iterator i$ = fileSet.getFileList().iterator();

      while(i$.hasNext()) {
         File f = (File)i$.next();
         files.add(new ScmFile(f.getPath(), ScmFileStatus.TAGGED));
      }

      return new TagScmResult("", files);
   }
}

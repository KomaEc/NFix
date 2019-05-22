package org.apache.maven.scm.provider.synergy.command.changelog;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.changelog.AbstractChangeLogCommand;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogSet;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.synergy.command.SynergyCommand;
import org.apache.maven.scm.provider.synergy.repository.SynergyScmProviderRepository;
import org.apache.maven.scm.provider.synergy.util.SynergyRole;
import org.apache.maven.scm.provider.synergy.util.SynergyTask;
import org.apache.maven.scm.provider.synergy.util.SynergyUtil;

public class SynergyChangeLogCommand extends AbstractChangeLogCommand implements SynergyCommand {
   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository repository, ScmFileSet fileSet, Date startDate, Date endDate, ScmBranch branch, String datePattern) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing changelog command...");
      }

      SynergyScmProviderRepository repo = (SynergyScmProviderRepository)repository;
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("basedir: " + fileSet.getBasedir());
      }

      String ccmAddr = SynergyUtil.start(this.getLogger(), repo.getUser(), repo.getPassword(), (SynergyRole)null);
      ArrayList csList = new ArrayList();

      try {
         String projectSpec = SynergyUtil.getWorkingProject(this.getLogger(), repo.getProjectSpec(), repo.getUser(), ccmAddr);
         if (projectSpec == null) {
            throw new ScmException("You should checkout a working project first");
         }

         List<SynergyTask> tasks = SynergyUtil.getCompletedTasks(this.getLogger(), projectSpec, startDate, endDate, ccmAddr);
         Iterator i$ = tasks.iterator();

         while(i$.hasNext()) {
            SynergyTask t = (SynergyTask)i$.next();
            ChangeSet cs = new ChangeSet();
            cs.setAuthor(t.getUsername());
            cs.setComment("Task " + t.getNumber() + ": " + t.getComment());
            cs.setDate(t.getModifiedTime());
            cs.setFiles(SynergyUtil.getModifiedObjects(this.getLogger(), t.getNumber(), ccmAddr));
            csList.add(cs);
         }
      } finally {
         SynergyUtil.stop(this.getLogger(), ccmAddr);
      }

      return new ChangeLogScmResult("ccm query ...", new ChangeLogSet(csList, startDate, endDate));
   }
}

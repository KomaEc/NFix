package org.apache.maven.scm.provider.hg.command.checkout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.command.checkout.AbstractCheckOutCommand;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.hg.HgUtils;
import org.apache.maven.scm.provider.hg.command.HgConsumer;
import org.apache.maven.scm.provider.hg.repository.HgScmProviderRepository;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;

public class HgCheckOutCommand extends AbstractCheckOutCommand implements Command {
   protected CheckOutScmResult executeCheckOutCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion scmVersion, boolean recursive) throws ScmException {
      HgScmProviderRepository repository = (HgScmProviderRepository)repo;
      String url = repository.getURI();
      File checkoutDir = fileSet.getBasedir();

      try {
         if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info("Removing " + checkoutDir);
         }

         FileUtils.deleteDirectory(checkoutDir);
      } catch (IOException var14) {
         throw new ScmException("Cannot remove " + checkoutDir);
      }

      List<String> cmdList = new ArrayList();
      if (repo.isPushChanges()) {
         cmdList.add("clone");
      } else {
         cmdList.add("update");
      }

      if (scmVersion != null && !StringUtils.isEmpty(scmVersion.getName())) {
         cmdList.add("-r");
         cmdList.add(scmVersion.getName());
      }

      if (!repo.isPushChanges()) {
         cmdList.add("-c");
      }

      cmdList.add(url);
      cmdList.add(checkoutDir.getAbsolutePath());
      String[] checkoutCmd = (String[])cmdList.toArray(new String[0]);
      HgConsumer checkoutConsumer = new HgConsumer(this.getLogger());
      HgUtils.execute(checkoutConsumer, this.getLogger(), checkoutDir.getParentFile(), checkoutCmd);
      String[] inventoryCmd = new String[]{"locate"};
      HgCheckOutConsumer consumer = new HgCheckOutConsumer(this.getLogger(), checkoutDir);
      ScmResult result = HgUtils.execute(consumer, this.getLogger(), checkoutDir, inventoryCmd);
      return new CheckOutScmResult(consumer.getCheckedOutFiles(), result);
   }
}

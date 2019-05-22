package org.apache.maven.scm.provider.bazaar.command.checkout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkout.AbstractCheckOutCommand;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.bazaar.BazaarUtils;
import org.apache.maven.scm.provider.bazaar.command.BazaarConsumer;
import org.apache.maven.scm.provider.bazaar.repository.BazaarScmProviderRepository;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;

public class BazaarCheckOutCommand extends AbstractCheckOutCommand {
   protected CheckOutScmResult executeCheckOutCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion version, boolean recursive) throws ScmException {
      BazaarScmProviderRepository repository = (BazaarScmProviderRepository)repo;
      String url = repository.getURI();
      File checkoutDir = fileSet.getBasedir();

      try {
         if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info("Removing " + checkoutDir);
         }

         FileUtils.deleteDirectory(checkoutDir);
      } catch (IOException var13) {
         throw new ScmException("Cannot remove " + checkoutDir);
      }

      List<String> checkoutCmd = new ArrayList();
      checkoutCmd.add("branch");
      checkoutCmd.add(url);
      checkoutCmd.add(checkoutDir.getAbsolutePath());
      if (version != null && StringUtils.isNotEmpty(version.getName())) {
         checkoutCmd.add("--revision");
         checkoutCmd.add("tag:" + version.getName());
      }

      BazaarConsumer checkoutConsumer = new BazaarConsumer(this.getLogger());
      BazaarUtils.execute(checkoutConsumer, this.getLogger(), checkoutDir.getParentFile(), (String[])((String[])checkoutCmd.toArray(new String[0])));
      String[] inventoryCmd = new String[]{"inventory"};
      BazaarCheckOutConsumer consumer = new BazaarCheckOutConsumer(this.getLogger(), checkoutDir);
      ScmResult result = BazaarUtils.execute(consumer, this.getLogger(), checkoutDir, inventoryCmd);
      if (!result.isSuccess()) {
         throw new ScmException(result.getProviderMessage());
      } else {
         return new CheckOutScmResult(consumer.getCheckedOutFiles(), result);
      }
   }
}

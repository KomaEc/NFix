package org.apache.maven.scm.provider.git.gitexe.command.info;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.info.InfoItem;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;
import org.codehaus.plexus.util.StringUtils;

public class GitInfoConsumer extends AbstractConsumer {
   private List<InfoItem> infoItems = new ArrayList(1);
   private ScmFileSet scmFileSet;

   public GitInfoConsumer(ScmLogger logger, ScmFileSet scmFileSet) {
      super(logger);
      this.scmFileSet = scmFileSet;
   }

   public void consumeLine(String line) {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("consume line " + line);
      }

      if (this.infoItems.isEmpty() && !StringUtils.isEmpty(line)) {
         InfoItem infoItem = new InfoItem();
         infoItem.setRevision(StringUtils.trim(line));
         infoItem.setURL(this.scmFileSet.getBasedir().getPath());
         this.infoItems.add(infoItem);
      }

   }

   public List<InfoItem> getInfoItems() {
      return this.infoItems;
   }
}

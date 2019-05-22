package org.apache.maven.scm.provider.hg.command.info;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.command.info.InfoItem;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.hg.command.HgConsumer;

public class HgInfoConsumer extends HgConsumer {
   private List<InfoItem> infoItems = new ArrayList(1);

   public HgInfoConsumer(ScmLogger scmLogger) {
      super(scmLogger);
   }

   public void consumeLine(String line) {
      InfoItem infoItem = new InfoItem();
      infoItem.setRevision(line);
      this.infoItems.add(infoItem);
   }

   public List<InfoItem> getInfoItems() {
      return this.infoItems;
   }
}

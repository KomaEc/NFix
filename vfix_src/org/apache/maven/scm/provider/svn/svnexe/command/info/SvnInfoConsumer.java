package org.apache.maven.scm.provider.svn.svnexe.command.info;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.command.info.InfoItem;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SvnInfoConsumer implements StreamConsumer {
   private List<InfoItem> infoItems = new ArrayList();
   private InfoItem currentItem = new InfoItem();

   public void consumeLine(String s) {
      if (s.equals("")) {
         if (this.currentItem != null) {
            this.infoItems.add(this.currentItem);
         }

         this.currentItem = new InfoItem();
      } else if (s.startsWith("Path: ")) {
         this.currentItem.setPath(getValue(s));
      } else if (s.startsWith("URL: ")) {
         this.currentItem.setURL(getValue(s));
      } else if (s.startsWith("Repository Root: ")) {
         this.currentItem.setRepositoryRoot(getValue(s));
      } else if (s.startsWith("Repository UUID: ")) {
         this.currentItem.setRepositoryUUID(getValue(s));
      } else if (s.startsWith("Revision: ")) {
         this.currentItem.setRevision(getValue(s));
      } else if (s.startsWith("Node Kind: ")) {
         this.currentItem.setNodeKind(getValue(s));
      } else if (s.startsWith("Schedule: ")) {
         this.currentItem.setSchedule(getValue(s));
      } else if (s.startsWith("Last Changed Author: ")) {
         this.currentItem.setLastChangedAuthor(getValue(s));
      } else if (s.startsWith("Last Changed Rev: ")) {
         this.currentItem.setLastChangedRevision(getValue(s));
      } else if (s.startsWith("Last Changed Date: ")) {
         this.currentItem.setLastChangedDate(getValue(s));
      }

   }

   private static String getValue(String s) {
      int idx = s.indexOf(": ");
      return idx < 0 ? null : s.substring(idx + 2);
   }

   public List<InfoItem> getInfoItems() {
      return this.infoItems;
   }
}

package org.apache.maven.scm.provider.accurev.cli;

import java.util.List;
import java.util.Map;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.accurev.FileDifference;

public class DiffConsumer extends XppStreamConsumer {
   private List<FileDifference> results;
   private FileDifference currentDifference;

   public DiffConsumer(ScmLogger logger, List<FileDifference> results) {
      super(logger);
      this.results = results;
   }

   protected void startTag(List<String> tagPath, Map<String, String> attributes) {
      String tagName = getTagName(tagPath);
      if ("Element".equals(tagName)) {
         this.currentDifference = new FileDifference();
      } else if ("Stream2".equals(tagName) && attributes.get("Name") != null) {
         this.currentDifference.setElementId(Long.parseLong((String)attributes.get("eid")));
         this.currentDifference.setNewVersion((String)attributes.get("Name"), (String)attributes.get("Version"));
      } else if ("Stream1".equals(tagName) && attributes.get("Name") != null) {
         this.currentDifference.setElementId(Long.parseLong((String)attributes.get("eid")));
         this.currentDifference.setOldVersion((String)attributes.get("Name"), (String)attributes.get("Version"));
      }

   }

   protected void endTag(List<String> tagPath) {
      String tagName = getTagName(tagPath);
      if ("Element".equals(tagName) && (this.currentDifference.getNewFile() != null || this.currentDifference.getOldFile() != null)) {
         this.results.add(this.currentDifference);
      }

   }
}

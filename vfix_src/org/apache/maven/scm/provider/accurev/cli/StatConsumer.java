package org.apache.maven.scm.provider.accurev.cli;

import java.util.List;
import java.util.Map;
import org.apache.maven.scm.log.ScmLogger;

public class StatConsumer extends XppStreamConsumer {
   private static final String ELEMENT_TAG = "element";
   private String status = null;

   public StatConsumer(ScmLogger logger) {
      super(logger);
   }

   public String getStatus() {
      return this.status;
   }

   protected void startTag(List<String> tagPath, Map<String, String> attributes) {
      int lastIndex = tagPath.size() - 1;
      if ("element".equalsIgnoreCase((String)tagPath.get(lastIndex))) {
         this.status = (String)attributes.get("status");
      }

   }
}

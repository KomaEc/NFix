package org.apache.maven.scm.provider.accurev.cli;

import java.util.List;
import java.util.Map;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.accurev.WorkSpace;

public class WorkSpaceConsumer extends XppStreamConsumer {
   private Map<String, WorkSpace> workSpaces;

   public WorkSpaceConsumer(ScmLogger logger, Map<String, WorkSpace> workSpaces) {
      super(logger);
      this.workSpaces = workSpaces;
   }

   protected void startTag(List<String> tagPath, Map<String, String> attributes) {
      if ("Element".equals(getTagName(tagPath))) {
         String name = (String)attributes.get("Name");
         long transactionId = Long.valueOf((String)attributes.get("Trans"));
         WorkSpace ws = new WorkSpace(name, transactionId);
         this.workSpaces.put(name, ws);
      }

   }
}

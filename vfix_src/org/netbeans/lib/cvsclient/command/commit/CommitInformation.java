package org.netbeans.lib.cvsclient.command.commit;

import org.netbeans.lib.cvsclient.command.DefaultFileInfoContainer;

public class CommitInformation extends DefaultFileInfoContainer {
   public static final String ADDED = "Added";
   public static final String REMOVED = "Removed";
   public static final String CHANGED = "Changed";
   public static final String UNKNOWN = "Unknown";
   public static final String TO_ADD = "To-be-added";
   private String revision;

   public String getRevision() {
      return this.revision;
   }

   public void setRevision(String var1) {
      this.revision = var1;
   }
}

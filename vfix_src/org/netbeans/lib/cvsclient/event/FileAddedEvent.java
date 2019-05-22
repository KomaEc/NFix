package org.netbeans.lib.cvsclient.event;

public class FileAddedEvent extends CVSEvent {
   protected String path;

   public FileAddedEvent(Object var1, String var2) {
      super(var1);
      this.path = var2;
   }

   public String getFilePath() {
      return this.path;
   }

   protected void fireEvent(CVSListener var1) {
      var1.fileAdded(this);
   }
}

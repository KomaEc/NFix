package org.netbeans.lib.cvsclient.event;

import org.netbeans.lib.cvsclient.command.FileInfoContainer;

public class FileInfoEvent extends CVSEvent {
   private final FileInfoContainer infoContainer;

   public FileInfoEvent(Object var1, FileInfoContainer var2) {
      super(var1);
      this.infoContainer = var2;
   }

   public FileInfoContainer getInfoContainer() {
      return this.infoContainer;
   }

   protected void fireEvent(CVSListener var1) {
      var1.fileInfoGenerated(this);
   }
}

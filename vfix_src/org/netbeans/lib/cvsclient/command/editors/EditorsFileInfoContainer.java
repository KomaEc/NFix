package org.netbeans.lib.cvsclient.command.editors;

import java.io.File;
import java.util.Date;
import org.netbeans.lib.cvsclient.command.FileInfoContainer;

public class EditorsFileInfoContainer extends FileInfoContainer {
   private final String client;
   private final Date date;
   private final File file;
   private final String user;

   EditorsFileInfoContainer(File var1, String var2, Date var3, String var4) {
      this.file = var1;
      this.user = var2;
      this.date = var3;
      this.client = var4;
   }

   public File getFile() {
      return this.file;
   }

   public String getClient() {
      return this.client;
   }

   public Date getDate() {
      return this.date;
   }

   public String getUser() {
      return this.user;
   }
}

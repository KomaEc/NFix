package org.netbeans.lib.cvsclient.response;

import java.io.File;
import java.io.IOException;
import org.netbeans.lib.cvsclient.admin.Entry;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class NewEntryResponse implements Response {
   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      try {
         String var3 = var1.readLine();
         String var4 = var1.readLine();
         String var5 = var1.readLine();
         File var6 = new File(var2.convertPathname(var3, var4));
         Entry var7 = new Entry(var5);
         var7.setConflict("dummy timestamp");
         var2.setEntry(var6, var7);
      } catch (IOException var8) {
         throw new ResponseException((Exception)var8.fillInStackTrace(), var8.getLocalizedMessage());
      }
   }

   public boolean isTerminalResponse() {
      return false;
   }
}

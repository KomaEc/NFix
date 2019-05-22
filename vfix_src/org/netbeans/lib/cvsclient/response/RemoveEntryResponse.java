package org.netbeans.lib.cvsclient.response;

import java.io.File;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class RemoveEntryResponse implements Response {
   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      try {
         String var3 = var1.readLine();
         String var4 = var1.readLine();
         String var5 = var2.convertPathname(var3, var4);
         File var6 = new File(var5);
         if (!var2.getGlobalOptions().isExcluded(var6)) {
            var2.removeEntry(var6);
         }
      } catch (Exception var7) {
         throw new ResponseException(var7);
      }
   }

   public boolean isTerminalResponse() {
      return false;
   }
}

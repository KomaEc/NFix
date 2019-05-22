package org.netbeans.lib.cvsclient.response;

import java.io.File;
import java.io.IOException;
import org.netbeans.lib.cvsclient.admin.Entry;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class ClearStaticDirectoryResponse implements Response {
   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      try {
         String var3 = var1.readLine();
         String var4 = var1.readLine();
         String var5 = var2.convertPathname(var3, var4);
         if (!var2.getGlobalOptions().isExcluded(new File(var5))) {
            var2.updateAdminData(var3, var4, (Entry)null);
            File var6 = new File(var5, "CVS/Entries.Static");
            if (var6.exists()) {
               var6.delete();
            }

         }
      } catch (IOException var7) {
         throw new ResponseException(var7);
      }
   }

   public boolean isTerminalResponse() {
      return false;
   }
}

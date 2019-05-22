package org.netbeans.lib.cvsclient.response;

import java.io.File;
import java.io.IOException;
import org.netbeans.lib.cvsclient.admin.Entry;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class SetStaticDirectoryResponse implements Response {
   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      try {
         String var3 = var1.readLine();
         String var4 = var1.readLine();
         var2.updateAdminData(var3, var4, (Entry)null);
         String var5 = var2.convertPathname(var3, var4);
         if (!var2.getGlobalOptions().isExcluded(new File(var5))) {
            var5 = var5 + "/CVS";
            File var6 = new File(var5);
            if (var6.exists()) {
               File var7 = new File(var5, "Entries.Static");
               var7.createNewFile();
            }

         }
      } catch (IOException var8) {
         throw new ResponseException(var8);
      }
   }

   public boolean isTerminalResponse() {
      return false;
   }
}

package org.netbeans.lib.cvsclient.response;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class SetStickyResponse implements Response {
   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      PrintWriter var3 = null;

      try {
         String var4 = var1.readLine();
         String var5 = var1.readLine();
         String var6 = var1.readLine();
         String var7 = var2.convertPathname(var4, var5);
         if (var2.getGlobalOptions().isExcluded(new File(var7))) {
            return;
         }

         var7 = var7 + "/CVS";
         File var8 = new File(var7);
         if (var8.exists()) {
            File var9 = new File(var7, "Tag");
            if ("THEAD".equals(var6) | "NHEAD".equals(var6)) {
               var9.delete();
            } else {
               var3 = new PrintWriter(new BufferedWriter(new FileWriter(var9)));
               var3.println(var6);
            }
         }
      } catch (IOException var13) {
         throw new ResponseException(var13);
      } finally {
         if (var3 != null) {
            var3.close();
         }

      }

   }

   public boolean isTerminalResponse() {
      return false;
   }
}

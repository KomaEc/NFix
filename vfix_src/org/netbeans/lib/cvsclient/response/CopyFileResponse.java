package org.netbeans.lib.cvsclient.response;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class CopyFileResponse implements Response {
   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      try {
         String var3 = var1.readLine();
         String var4 = var1.readLine();
         String var5 = var1.readLine();
         String var6 = var2.convertPathname(var3, var4);
         if (!var2.getGlobalOptions().isExcluded(new File(var6))) {
            if (!var2.getGlobalOptions().isDoNoChanges()) {
               var2.removeLocalFile((new File((new File(var6)).getParentFile(), var5)).getAbsolutePath());
               var2.renameLocalFile(var6, var5);
            }

         }
      } catch (EOFException var7) {
         throw new ResponseException(var7, ResponseException.getLocalMessage("CommandException.EndOfFile", (Object[])null));
      } catch (IOException var8) {
         throw new ResponseException(var8);
      }
   }

   public boolean isTerminalResponse() {
      return false;
   }
}

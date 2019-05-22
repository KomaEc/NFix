package org.netbeans.lib.cvsclient.response;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import org.netbeans.lib.cvsclient.event.FileRemovedEvent;
import org.netbeans.lib.cvsclient.event.FileToRemoveEvent;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class RemovedResponse implements Response {
   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      try {
         String var3 = var1.readLine();
         String var4 = var1.readLine();
         String var5 = var2.convertPathname(var3, var4);
         var5 = (new File(var5)).getAbsolutePath();
         if (!var2.getGlobalOptions().isExcluded(new File(var5))) {
            FileToRemoveEvent var6 = new FileToRemoveEvent(this, var5);
            FileRemovedEvent var7 = new FileRemovedEvent(this, var5);
            var2.getEventManager().fireCVSEvent(var6);
            var2.removeLocalFile(var3, var4);
            var2.getEventManager().fireCVSEvent(var7);
         }
      } catch (EOFException var8) {
         throw new ResponseException(var8, ResponseException.getLocalMessage("CommandException.EndOfFile", (Object[])null));
      } catch (IOException var9) {
         throw new ResponseException(var9);
      }
   }

   public boolean isTerminalResponse() {
      return false;
   }
}

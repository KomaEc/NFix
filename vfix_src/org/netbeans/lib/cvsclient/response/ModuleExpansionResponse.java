package org.netbeans.lib.cvsclient.response;

import java.io.IOException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.ModuleExpansionEvent;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class ModuleExpansionResponse implements Response {
   public ModuleExpansionResponse() {
   }

   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      try {
         String var3 = var1.readLine();
         EventManager var4 = var2.getEventManager();
         var4.fireCVSEvent(new ModuleExpansionEvent(this, var3));
      } catch (IOException var5) {
         throw new ResponseException(var5);
      }
   }

   public boolean isTerminalResponse() {
      return false;
   }
}

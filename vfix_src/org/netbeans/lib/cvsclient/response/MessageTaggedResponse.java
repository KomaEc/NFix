package org.netbeans.lib.cvsclient.response;

import java.io.EOFException;
import java.io.IOException;
import org.netbeans.lib.cvsclient.event.MessageEvent;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class MessageTaggedResponse implements Response {
   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      try {
         String var3 = var1.readLine();
         MessageEvent var4 = new MessageEvent(this, var3, false);
         var4.setTagged(true);
         var2.getEventManager().fireCVSEvent(var4);
      } catch (EOFException var5) {
         throw new ResponseException(var5, ResponseException.getLocalMessage("CommandException.EndOfFile", (Object[])null));
      } catch (IOException var6) {
         throw new ResponseException(var6);
      }
   }

   public boolean isTerminalResponse() {
      return false;
   }
}

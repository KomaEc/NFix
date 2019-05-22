package org.netbeans.lib.cvsclient.response;

import java.io.EOFException;
import java.io.IOException;
import org.netbeans.lib.cvsclient.event.MessageEvent;
import org.netbeans.lib.cvsclient.event.TerminationEvent;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

public class ErrorResponse implements Response {
   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      try {
         MessageEvent var3 = new MessageEvent(this, var1.readLine(), true);
         var2.getEventManager().fireCVSEvent(var3);
         TerminationEvent var4 = new TerminationEvent(this, true);
         var2.getEventManager().fireCVSEvent(var4);
      } catch (EOFException var5) {
         throw new ResponseException(var5, ResponseException.getLocalMessage("CommandException.EndOfFile", (Object[])null));
      } catch (IOException var6) {
         throw new ResponseException(var6);
      }
   }

   public boolean isTerminalResponse() {
      return true;
   }
}

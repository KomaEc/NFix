package org.netbeans.lib.cvsclient.response;

import java.io.EOFException;
import java.io.IOException;
import org.netbeans.lib.cvsclient.event.MessageEvent;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

public final class ErrorMessageResponse implements Response {
   private boolean terminating;
   private String message;

   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      try {
         String var3 = var1.readLine();
         this.terminating |= var3.endsWith(" [server aborted]: received termination signal");
         this.terminating |= var3.endsWith(" [server aborted]: received broken pipe signal");
         this.terminating |= var3.endsWith(" [checkout aborted]: end of file from server (consult above messages if any)");
         this.terminating &= var1.available() == 0;
         this.message = var3;
         MessageEvent var4 = new MessageEvent(this, var3, true);
         var2.getEventManager().fireCVSEvent(var4);
      } catch (EOFException var5) {
         throw new ResponseException(var5, ResponseException.getLocalMessage("CommandException.EndOfFile", (Object[])null));
      } catch (IOException var6) {
         throw new ResponseException(var6);
      }
   }

   public boolean isTerminalResponse() {
      return this.terminating;
   }

   public String getMessage() {
      return this.message;
   }
}

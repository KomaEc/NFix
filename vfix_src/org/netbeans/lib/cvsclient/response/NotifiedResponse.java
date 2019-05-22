package org.netbeans.lib.cvsclient.response;

import java.io.EOFException;
import java.io.IOException;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class NotifiedResponse implements Response {
   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      try {
         var1.readLine();
         var1.readLine();
      } catch (EOFException var4) {
         throw new ResponseException(var4, ResponseException.getLocalMessage("CommandException.EndOfFile", (Object[])null));
      } catch (IOException var5) {
         throw new ResponseException(var5);
      }
   }

   public boolean isTerminalResponse() {
      return false;
   }
}

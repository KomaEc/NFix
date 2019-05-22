package org.netbeans.lib.cvsclient.response;

import java.io.EOFException;
import java.io.IOException;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class ValidRequestsResponse implements Response {
   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      try {
         String var3 = var1.readLine();
         var2.setValidRequests(var3);
         if (var3.indexOf("gzip-file-contents") < 0) {
            var2.dontUseGzipFileHandler();
         }

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

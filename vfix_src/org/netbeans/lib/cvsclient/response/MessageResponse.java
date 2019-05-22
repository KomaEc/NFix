package org.netbeans.lib.cvsclient.response;

import java.io.EOFException;
import java.io.IOException;
import org.netbeans.lib.cvsclient.event.MessageEvent;
import org.netbeans.lib.cvsclient.util.ByteArray;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class MessageResponse implements Response {
   private boolean terminating = false;
   private String firstWord;

   public MessageResponse() {
   }

   public MessageResponse(String var1) {
      this.firstWord = var1;
   }

   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      try {
         ByteArray var3 = var1.readLineBytes();
         String var4 = var3.getStringFromBytes();
         if (this.firstWord != null) {
            var4 = this.firstWord + " " + var4;
         }

         this.terminating |= var4.endsWith(" [server aborted]: received termination signal");
         this.terminating |= var4.endsWith(" [server aborted]: received broken pipe signal");
         this.terminating |= var4.endsWith(" [checkout aborted]: end of file from server (consult above messages if any)");
         this.terminating &= var1.available() == 0;
         MessageEvent var5 = new MessageEvent(this, var4, var3.getBytes(), false);
         var2.getEventManager().fireCVSEvent(var5);
      } catch (EOFException var6) {
         throw new ResponseException(var6, ResponseException.getLocalMessage("CommandException.EndOfFile", (Object[])null));
      } catch (IOException var7) {
         throw new ResponseException(var7);
      }
   }

   public boolean isTerminalResponse() {
      return this.terminating;
   }
}

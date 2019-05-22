package org.netbeans.lib.cvsclient.response;

import java.io.EOFException;
import java.io.IOException;
import org.netbeans.lib.cvsclient.event.BinaryMessageEvent;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class MessageBinaryResponse implements Response {
   private static final int CHUNK_SIZE = 262144;

   public MessageBinaryResponse() {
   }

   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      try {
         String var3 = var1.readLine();

         int var4;
         try {
            var4 = Integer.parseInt(var3);
         } catch (NumberFormatException var9) {
            throw new ResponseException(var9);
         }

         int var5 = Math.min(var4, 262144);
         byte[] var6 = new byte[var5];

         while(var4 > 0) {
            int var7 = var1.read(var6, 0, var5);
            if (var7 == -1) {
               throw new ResponseException("EOF", ResponseException.getLocalMessage("CommandException.EndOfFile", (Object[])null));
            }

            var4 -= var7;
            var5 = Math.min(var4, 262144);
            BinaryMessageEvent var8 = new BinaryMessageEvent(this, var6, var7);
            var2.getEventManager().fireCVSEvent(var8);
         }

      } catch (EOFException var10) {
         throw new ResponseException(var10, ResponseException.getLocalMessage("CommandException.EndOfFile", (Object[])null));
      } catch (IOException var11) {
         throw new ResponseException(var11);
      }
   }

   public boolean isTerminalResponse() {
      return false;
   }
}

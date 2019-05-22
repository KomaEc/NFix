package com.gzoltar.shaded.org.pitest.util;

import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;

class SocketReadingCallable implements Callable<ExitCode> {
   private final SideEffect1<SafeDataOutputStream> sendInitialData;
   private final ReceiveStrategy receive;
   private final ServerSocket socket;

   public SocketReadingCallable(ServerSocket socket, SideEffect1<SafeDataOutputStream> sendInitialData, ReceiveStrategy receive) {
      this.socket = socket;
      this.sendInitialData = sendInitialData;
      this.receive = receive;
   }

   public ExitCode call() throws Exception {
      Socket clientSocket = this.socket.accept();
      ExitCode exitCode = ExitCode.UNKNOWN_ERROR;

      try {
         BufferedInputStream bif = new BufferedInputStream(clientSocket.getInputStream());
         this.sendDataToSlave(clientSocket);
         SafeDataInputStream is = new SafeDataInputStream(bif);
         exitCode = this.receiveResults(is);
         bif.close();
      } catch (IOException var12) {
         throw Unchecked.translateCheckedException(var12);
      } finally {
         try {
            if (clientSocket != null) {
               clientSocket.close();
            }

            this.socket.close();
         } catch (IOException var11) {
            throw Unchecked.translateCheckedException(var11);
         }
      }

      return exitCode;
   }

   private void sendDataToSlave(Socket clientSocket) throws IOException {
      OutputStream os = clientSocket.getOutputStream();
      SafeDataOutputStream dos = new SafeDataOutputStream(os);
      this.sendInitialData.apply(dos);
   }

   private ExitCode receiveResults(SafeDataInputStream is) {
      for(byte control = is.readByte(); control != 64; control = is.readByte()) {
         this.receive.apply(control, is);
      }

      return ExitCode.fromCode(is.readInt());
   }
}

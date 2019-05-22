package com.gzoltar.shaded.org.jacoco.core.tools;

import com.gzoltar.shaded.org.jacoco.core.runtime.RemoteControlReader;
import com.gzoltar.shaded.org.jacoco.core.runtime.RemoteControlWriter;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.Socket;

public class ExecDumpClient {
   private boolean dump = true;
   private boolean reset = false;
   private int retryCount = 0;
   private long retryDelay;

   public ExecDumpClient() {
      this.setRetryDelay(1000L);
   }

   public void setDump(boolean dump) {
      this.dump = dump;
   }

   public void setReset(boolean reset) {
      this.reset = reset;
   }

   public void setRetryCount(int retryCount) {
      this.retryCount = retryCount;
   }

   public void setRetryDelay(long retryDelay) {
      this.retryDelay = retryDelay;
   }

   public ExecFileLoader dump(String address, int port) throws IOException {
      return this.dump(InetAddress.getByName(address), port);
   }

   public ExecFileLoader dump(InetAddress address, int port) throws IOException {
      ExecFileLoader loader = new ExecFileLoader();
      Socket socket = this.tryConnect(address, port);

      try {
         RemoteControlWriter remoteWriter = new RemoteControlWriter(socket.getOutputStream());
         RemoteControlReader remoteReader = new RemoteControlReader(socket.getInputStream());
         remoteReader.setSessionInfoVisitor(loader.getSessionInfoStore());
         remoteReader.setExecutionDataVisitor(loader.getExecutionDataStore());
         remoteWriter.visitDumpCommand(this.dump, this.reset);
         remoteReader.read();
      } finally {
         socket.close();
      }

      return loader;
   }

   private Socket tryConnect(InetAddress address, int port) throws IOException {
      int count = 0;

      while(true) {
         try {
            this.onConnecting(address, port);
            return new Socket(address, port);
         } catch (IOException var5) {
            ++count;
            if (count > this.retryCount) {
               throw var5;
            }

            this.onConnectionFailure(var5);
            this.sleep();
         }
      }
   }

   private void sleep() throws InterruptedIOException {
      try {
         Thread.sleep(this.retryDelay);
      } catch (InterruptedException var2) {
         throw new InterruptedIOException();
      }
   }

   protected void onConnecting(InetAddress address, int port) {
   }

   protected void onConnectionFailure(IOException exception) {
   }
}

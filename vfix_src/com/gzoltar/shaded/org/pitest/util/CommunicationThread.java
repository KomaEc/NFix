package com.gzoltar.shaded.org.pitest.util;

import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommunicationThread {
   private static final Logger LOG = Log.getLogger();
   private final SideEffect1<SafeDataOutputStream> sendInitialData;
   private final ReceiveStrategy receive;
   private final ServerSocket socket;
   private FutureTask<ExitCode> future;

   public CommunicationThread(ServerSocket socket, SideEffect1<SafeDataOutputStream> sendInitialData, ReceiveStrategy receive) {
      this.socket = socket;
      this.sendInitialData = sendInitialData;
      this.receive = receive;
   }

   public void start() throws IOException, InterruptedException {
      this.future = this.createFuture();
   }

   private FutureTask<ExitCode> createFuture() {
      FutureTask<ExitCode> newFuture = new FutureTask(new SocketReadingCallable(this.socket, this.sendInitialData, this.receive));
      Thread thread = new Thread(newFuture);
      thread.setDaemon(true);
      thread.setName("pit communication");
      thread.start();
      return newFuture;
   }

   public ExitCode waitToFinish() {
      try {
         return (ExitCode)this.future.get();
      } catch (ExecutionException var2) {
         LOG.log(Level.WARNING, "Error while watching child process", var2);
         return ExitCode.UNKNOWN_ERROR;
      } catch (InterruptedException var3) {
         LOG.log(Level.WARNING, "interrupted while waiting for child process", var3);
         return ExitCode.UNKNOWN_ERROR;
      }
   }
}

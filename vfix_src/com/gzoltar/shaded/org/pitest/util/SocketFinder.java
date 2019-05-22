package com.gzoltar.shaded.org.pitest.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

public class SocketFinder {
   private static final Logger LOG = Log.getLogger();
   private static final int MIN_PORT_NUMBER = 8091;
   private static final int MAX_PORT_NUMBER = 9000;
   private int lastPortNumber = 8091;

   public synchronized ServerSocket getNextAvailableServerSocket() {
      ++this.lastPortNumber;

      ServerSocket socket;
      for(socket = getIfAvailable(this.lastPortNumber); socket == null; socket = getIfAvailable(this.lastPortNumber)) {
         ++this.lastPortNumber;
         if (this.lastPortNumber > 9000) {
            this.lastPortNumber = 9000;
         }
      }

      LOG.fine("using port " + this.lastPortNumber);
      return socket;
   }

   private static synchronized ServerSocket getIfAvailable(int port) {
      ServerSocket ss = null;

      try {
         ss = new ServerSocket(port);
      } catch (IOException var3) {
         LOG.fine("port " + port + " is in use");
      }

      return ss;
   }
}

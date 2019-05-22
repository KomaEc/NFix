package org.jboss.net.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeoutServerSocket extends ServerSocket {
   public TimeoutServerSocket(int port) throws IOException {
      this(port, 50);
   }

   public TimeoutServerSocket(int port, int backlog) throws IOException {
      this(port, backlog, (InetAddress)null);
   }

   public TimeoutServerSocket(int port, int backlog, InetAddress bindAddr) throws IOException {
      super(port, backlog, bindAddr);
   }

   public Socket accept() throws IOException {
      Socket s = super.accept();
      s.setSoTimeout(1000);
      TimeoutSocket ts = new TimeoutSocket(s);
      return ts;
   }
}

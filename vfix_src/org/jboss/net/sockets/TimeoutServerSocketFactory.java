package org.jboss.net.sockets;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.rmi.server.RMIServerSocketFactory;

public class TimeoutServerSocketFactory implements RMIServerSocketFactory, Serializable {
   static final long serialVersionUID = 7006964274840965634L;
   protected transient InetAddress bindAddress;
   protected int backlog;

   public TimeoutServerSocketFactory() {
      this((InetAddress)null, 200);
   }

   public TimeoutServerSocketFactory(InetAddress bindAddress) {
      this(bindAddress, 200);
   }

   public TimeoutServerSocketFactory(int backlog) {
      this((InetAddress)null, backlog);
   }

   public TimeoutServerSocketFactory(InetAddress bindAddress, int backlog) {
      this.backlog = 200;
      this.bindAddress = bindAddress;
      this.backlog = backlog;
   }

   public String getBindAddress() {
      String address = null;
      if (this.bindAddress != null) {
         address = this.bindAddress.getHostAddress();
      }

      return address;
   }

   public void setBindAddress(String host) throws UnknownHostException {
      this.bindAddress = InetAddress.getByName(host);
   }

   public void setBindAddress(InetAddress bindAddress) {
      this.bindAddress = bindAddress;
   }

   public ServerSocket createServerSocket(int port) throws IOException {
      ServerSocket activeSocket = new TimeoutServerSocket(port, this.backlog, this.bindAddress);
      return activeSocket;
   }

   public boolean equals(Object obj) {
      return obj instanceof TimeoutServerSocketFactory;
   }

   public int hashCode() {
      return this.getClass().getName().hashCode();
   }
}

package org.jboss.net.sockets;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.rmi.server.RMIServerSocketFactory;
import javax.net.ServerSocketFactory;

public class DefaultSocketFactory extends ServerSocketFactory implements RMIServerSocketFactory, Serializable {
   static final long serialVersionUID = -7626239955727142958L;
   private transient InetAddress bindAddress;
   private int backlog;

   public DefaultSocketFactory() {
      this((InetAddress)null, 200);
   }

   public DefaultSocketFactory(InetAddress bindAddress) {
      this(bindAddress, 200);
   }

   public DefaultSocketFactory(int backlog) {
      this((InetAddress)null, backlog);
   }

   public DefaultSocketFactory(InetAddress bindAddress, int backlog) {
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

   public ServerSocket createServerSocket(int port) throws IOException {
      return this.createServerSocket(port, this.backlog, this.bindAddress);
   }

   public ServerSocket createServerSocket(int port, int backlog) throws IOException {
      return this.createServerSocket(port, backlog, (InetAddress)null);
   }

   public ServerSocket createServerSocket(int port, int backlog, InetAddress inetAddress) throws IOException {
      ServerSocket activeSocket = new ServerSocket(port, backlog, this.bindAddress);
      return activeSocket;
   }

   public boolean equals(Object obj) {
      boolean equals = obj instanceof DefaultSocketFactory;
      if (equals && this.bindAddress != null) {
         DefaultSocketFactory dsf = (DefaultSocketFactory)obj;
         InetAddress dsfa = dsf.bindAddress;
         if (dsfa != null) {
            equals = this.bindAddress.equals(dsfa);
         } else {
            equals = false;
         }
      }

      return equals;
   }

   public int hashCode() {
      int hashCode = this.getClass().getName().hashCode();
      if (this.bindAddress != null) {
         hashCode += this.bindAddress.toString().hashCode();
      }

      return hashCode;
   }

   public String toString() {
      StringBuffer tmp = new StringBuffer(super.toString());
      tmp.append('[');
      tmp.append("bindAddress=");
      tmp.append(this.bindAddress);
      tmp.append(']');
      return tmp.toString();
   }
}

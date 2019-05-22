package org.jboss.net.sockets;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.server.RMIClientSocketFactory;

public class DefaultClientSocketFactory implements RMIClientSocketFactory, Serializable {
   private static final long serialVersionUID = -920483051658660269L;
   private InetAddress bindAddress;

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

   public Socket createSocket(String host, int port) throws IOException {
      InetAddress addr = null;
      if (this.bindAddress != null) {
         addr = this.bindAddress;
      } else {
         addr = InetAddress.getByName(host);
      }

      Socket s = new Socket(addr, port);
      return s;
   }

   public boolean equals(Object obj) {
      boolean equals = obj instanceof DefaultClientSocketFactory;
      if (equals && this.bindAddress != null) {
         DefaultClientSocketFactory dcsf = (DefaultClientSocketFactory)obj;
         InetAddress dcsfa = dcsf.bindAddress;
         if (dcsfa != null) {
            equals = this.bindAddress.equals(dcsfa);
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

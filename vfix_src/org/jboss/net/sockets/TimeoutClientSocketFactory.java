package org.jboss.net.sockets;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;

public class TimeoutClientSocketFactory implements RMIClientSocketFactory, Serializable {
   private static final long serialVersionUID = -920483051658660269L;

   public Socket createSocket(String host, int port) throws IOException {
      Socket s = new Socket(host, port);
      s.setSoTimeout(1000);
      TimeoutSocket ts = new TimeoutSocket(s);
      return ts;
   }

   public boolean equals(Object obj) {
      return obj instanceof TimeoutClientSocketFactory;
   }

   public int hashCode() {
      return this.getClass().getName().hashCode();
   }
}

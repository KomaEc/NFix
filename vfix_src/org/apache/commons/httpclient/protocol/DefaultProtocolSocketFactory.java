package org.apache.commons.httpclient.protocol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class DefaultProtocolSocketFactory implements ProtocolSocketFactory {
   private static final DefaultProtocolSocketFactory factory = new DefaultProtocolSocketFactory();
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$protocol$DefaultProtocolSocketFactory;

   static DefaultProtocolSocketFactory getSocketFactory() {
      return factory;
   }

   public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {
      return new Socket(host, port, clientHost, clientPort);
   }

   public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
      return new Socket(host, port);
   }

   public boolean equals(Object obj) {
      return obj != null && obj.getClass().equals(class$org$apache$commons$httpclient$protocol$DefaultProtocolSocketFactory == null ? (class$org$apache$commons$httpclient$protocol$DefaultProtocolSocketFactory = class$("org.apache.commons.httpclient.protocol.DefaultProtocolSocketFactory")) : class$org$apache$commons$httpclient$protocol$DefaultProtocolSocketFactory);
   }

   public int hashCode() {
      return (class$org$apache$commons$httpclient$protocol$DefaultProtocolSocketFactory == null ? (class$org$apache$commons$httpclient$protocol$DefaultProtocolSocketFactory = class$("org.apache.commons.httpclient.protocol.DefaultProtocolSocketFactory")) : class$org$apache$commons$httpclient$protocol$DefaultProtocolSocketFactory).hashCode();
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}

package org.apache.commons.httpclient.protocol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocketFactory;

public class SSLProtocolSocketFactory implements SecureProtocolSocketFactory {
   private static final SSLProtocolSocketFactory factory = new SSLProtocolSocketFactory();
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$protocol$SSLProtocolSocketFactory;

   static SSLProtocolSocketFactory getSocketFactory() {
      return factory;
   }

   public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {
      return SSLSocketFactory.getDefault().createSocket(host, port, clientHost, clientPort);
   }

   public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
      return SSLSocketFactory.getDefault().createSocket(host, port);
   }

   public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
      return ((SSLSocketFactory)SSLSocketFactory.getDefault()).createSocket(socket, host, port, autoClose);
   }

   public boolean equals(Object obj) {
      return obj != null && obj.getClass().equals(class$org$apache$commons$httpclient$protocol$SSLProtocolSocketFactory == null ? (class$org$apache$commons$httpclient$protocol$SSLProtocolSocketFactory = class$("org.apache.commons.httpclient.protocol.SSLProtocolSocketFactory")) : class$org$apache$commons$httpclient$protocol$SSLProtocolSocketFactory);
   }

   public int hashCode() {
      return (class$org$apache$commons$httpclient$protocol$SSLProtocolSocketFactory == null ? (class$org$apache$commons$httpclient$protocol$SSLProtocolSocketFactory = class$("org.apache.commons.httpclient.protocol.SSLProtocolSocketFactory")) : class$org$apache$commons$httpclient$protocol$SSLProtocolSocketFactory).hashCode();
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

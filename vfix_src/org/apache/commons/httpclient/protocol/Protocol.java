package org.apache.commons.httpclient.protocol;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Protocol {
   private static final Map PROTOCOLS = Collections.synchronizedMap(new HashMap());
   private String scheme;
   private ProtocolSocketFactory socketFactory;
   private int defaultPort;
   private boolean secure;

   public static void registerProtocol(String id, Protocol protocol) {
      if (id == null) {
         throw new IllegalArgumentException("id is null");
      } else if (protocol == null) {
         throw new IllegalArgumentException("protocol is null");
      } else {
         PROTOCOLS.put(id, protocol);
      }
   }

   public static void unregisterProtocol(String id) {
      if (id == null) {
         throw new IllegalArgumentException("id is null");
      } else {
         PROTOCOLS.remove(id);
      }
   }

   public static Protocol getProtocol(String id) throws IllegalStateException {
      if (id == null) {
         throw new IllegalArgumentException("id is null");
      } else {
         Protocol protocol = (Protocol)PROTOCOLS.get(id);
         if (protocol == null) {
            protocol = lazyRegisterProtocol(id);
         }

         return protocol;
      }
   }

   private static Protocol lazyRegisterProtocol(String id) throws IllegalStateException {
      Protocol https;
      if ("http".equals(id)) {
         https = new Protocol("http", DefaultProtocolSocketFactory.getSocketFactory(), 80);
         registerProtocol("http", https);
         return https;
      } else if ("https".equals(id)) {
         https = new Protocol("https", SSLProtocolSocketFactory.getSocketFactory(), 443);
         registerProtocol("https", https);
         return https;
      } else {
         throw new IllegalStateException("unsupported protocol: '" + id + "'");
      }
   }

   public Protocol(String scheme, ProtocolSocketFactory factory, int defaultPort) {
      if (scheme == null) {
         throw new IllegalArgumentException("scheme is null");
      } else if (factory == null) {
         throw new IllegalArgumentException("socketFactory is null");
      } else if (defaultPort <= 0) {
         throw new IllegalArgumentException("port is invalid: " + defaultPort);
      } else {
         this.scheme = scheme;
         this.socketFactory = factory;
         this.defaultPort = defaultPort;
         this.secure = false;
      }
   }

   public Protocol(String scheme, SecureProtocolSocketFactory factory, int defaultPort) {
      if (scheme == null) {
         throw new IllegalArgumentException("scheme is null");
      } else if (factory == null) {
         throw new IllegalArgumentException("socketFactory is null");
      } else if (defaultPort <= 0) {
         throw new IllegalArgumentException("port is invalid: " + defaultPort);
      } else {
         this.scheme = scheme;
         this.socketFactory = factory;
         this.defaultPort = defaultPort;
         this.secure = true;
      }
   }

   public int getDefaultPort() {
      return this.defaultPort;
   }

   public ProtocolSocketFactory getSocketFactory() {
      return this.socketFactory;
   }

   public String getScheme() {
      return this.scheme;
   }

   public boolean isSecure() {
      return this.secure;
   }

   public int resolvePort(int port) {
      return port <= 0 ? this.getDefaultPort() : port;
   }

   public String toString() {
      return this.scheme + ":" + this.defaultPort;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof Protocol)) {
         return false;
      } else {
         Protocol p = (Protocol)obj;
         return this.defaultPort == p.getDefaultPort() && this.scheme.equalsIgnoreCase(p.getScheme()) && this.secure == p.isSecure() && this.socketFactory.equals(p.getSocketFactory());
      }
   }

   public int hashCode() {
      return this.scheme.hashCode();
   }
}

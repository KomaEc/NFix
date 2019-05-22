package org.apache.commons.httpclient;

import java.io.IOException;
import java.io.InputStream;

public class SimpleHttpConnectionManager implements HttpConnectionManager {
   private HttpConnection httpConnection;
   private boolean connectionStaleCheckingEnabled = true;

   public HttpConnection getConnection(HostConfiguration hostConfiguration) {
      return this.getConnection(hostConfiguration, 0L);
   }

   public boolean isConnectionStaleCheckingEnabled() {
      return this.connectionStaleCheckingEnabled;
   }

   public void setConnectionStaleCheckingEnabled(boolean connectionStaleCheckingEnabled) {
      this.connectionStaleCheckingEnabled = connectionStaleCheckingEnabled;
   }

   public HttpConnection getConnection(HostConfiguration hostConfiguration, long timeout) {
      if (this.httpConnection == null) {
         this.httpConnection = new HttpConnection(hostConfiguration);
         this.httpConnection.setStaleCheckingEnabled(this.connectionStaleCheckingEnabled);
      } else if (hostConfiguration.hostEquals(this.httpConnection) && hostConfiguration.proxyEquals(this.httpConnection)) {
         finishLastResponse(this.httpConnection);
      } else {
         if (this.httpConnection.isOpen()) {
            this.httpConnection.close();
         }

         this.httpConnection.setStaleCheckingEnabled(this.connectionStaleCheckingEnabled);
         this.httpConnection.setHost(hostConfiguration.getHost());
         this.httpConnection.setVirtualHost(hostConfiguration.getVirtualHost());
         this.httpConnection.setPort(hostConfiguration.getPort());
         this.httpConnection.setProtocol(hostConfiguration.getProtocol());
         this.httpConnection.setLocalAddress(hostConfiguration.getLocalAddress());
         this.httpConnection.setProxyHost(hostConfiguration.getProxyHost());
         this.httpConnection.setProxyPort(hostConfiguration.getProxyPort());
      }

      return this.httpConnection;
   }

   public void releaseConnection(HttpConnection conn) {
      if (conn != this.httpConnection) {
         throw new IllegalStateException("Unexpected close on a different connection.");
      } else {
         finishLastResponse(this.httpConnection);
      }
   }

   static void finishLastResponse(HttpConnection conn) {
      InputStream lastResponse = conn.getLastResponseInputStream();
      if (lastResponse != null) {
         conn.setLastResponseInputStream((InputStream)null);

         try {
            lastResponse.close();
         } catch (IOException var3) {
            conn.close();
         }
      }

   }
}

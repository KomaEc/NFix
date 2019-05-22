package org.apache.commons.httpclient;

import java.io.IOException;
import java.net.URL;
import java.security.Provider;
import java.security.Security;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpClient {
   private static final Log LOG;
   private HttpConnectionManager httpConnectionManager;
   private HttpState state;
   private long httpConnectionTimeout;
   private int timeoutInMilliseconds;
   private int connectionTimeout;
   private HostConfiguration hostConfiguration;
   private boolean strictMode;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$HttpClient;

   public HttpClient() {
      this(new SimpleHttpConnectionManager());
   }

   public HttpClient(HttpConnectionManager httpConnectionManager) {
      this.httpConnectionTimeout = 0L;
      this.timeoutInMilliseconds = 0;
      this.connectionTimeout = 0;
      this.strictMode = false;
      if (httpConnectionManager == null) {
         throw new IllegalArgumentException("httpConnectionManager cannot be null");
      } else {
         this.state = new HttpState();
         this.httpConnectionManager = httpConnectionManager;
         this.hostConfiguration = new HostConfiguration();
      }
   }

   public synchronized HttpState getState() {
      return this.state;
   }

   public synchronized void setState(HttpState state) {
      this.state = state;
   }

   public synchronized void setStrictMode(boolean strictMode) {
      this.strictMode = strictMode;
   }

   public synchronized boolean isStrictMode() {
      return this.strictMode;
   }

   public synchronized void setTimeout(int newTimeoutInMilliseconds) {
      this.timeoutInMilliseconds = newTimeoutInMilliseconds;
   }

   public synchronized void setHttpConnectionFactoryTimeout(long timeout) {
      this.httpConnectionTimeout = timeout;
   }

   public synchronized void setConnectionTimeout(int newTimeoutInMilliseconds) {
      this.connectionTimeout = newTimeoutInMilliseconds;
   }

   /** @deprecated */
   public void startSession(String host, int port) {
      LOG.trace("enter HttpClient.startSession(String, int)");
      this.startSession(host, port, false);
   }

   /** @deprecated */
   public void startSession(String host, int port, boolean https) {
      LOG.trace("enter HttpClient.startSession(String, int, boolean)");
      if (LOG.isDebugEnabled()) {
         LOG.debug("HttpClient.startSession(String,int,boolean): Host:" + host + " Port:" + port + " HTTPS:" + https);
      }

      this.hostConfiguration.setHost(host, port, https ? "https" : "http");
   }

   /** @deprecated */
   public void startSession(String host, int port, Credentials creds) {
      LOG.trace("enter HttpClient.startSession(String, int, Credentials)");
      this.startSession(host, port, creds, false);
   }

   /** @deprecated */
   public void startSession(String host, int port, Credentials creds, boolean https) {
      LOG.trace("enter HttpClient.startSession(String, int, Credentials, boolean)");
      if (LOG.isDebugEnabled()) {
         LOG.debug("Starting HttpClient session Host:" + host + " Port:" + port + " Credentials:" + creds + " HTTPS:" + https);
      }

      this.getState().setCredentials((String)null, creds);
      this.hostConfiguration.setHost(host, port, https ? "https" : "http");
   }

   /** @deprecated */
   public void startSession(URI uri) throws URIException, IllegalStateException {
      LOG.trace("enter HttpClient.startSession(URI)");
      String scheme = uri.getScheme();
      if (scheme == null) {
         LOG.error("no scheme to start a session");
         throw new IllegalStateException("no scheme to start a session");
      } else {
         Protocol protocol = Protocol.getProtocol(scheme);
         String userinfo = uri.getUserinfo();
         if (userinfo != null) {
            this.getState().setCredentials((String)null, new UsernamePasswordCredentials(userinfo));
         }

         String host = uri.getHost();
         if (host != null && host.length() != 0) {
            int port = uri.getPort();
            if (port == -1) {
               LOG.error("HttpURL or HttpsURL instance required");
               throw new IllegalStateException("HttpURL or HttpsURL instance required");
            } else {
               this.hostConfiguration.setHost(host, (String)null, port, protocol);
            }
         } else {
            LOG.error("no host to start a session");
            throw new IllegalStateException("no host to start a session");
         }
      }
   }

   /** @deprecated */
   public void startSession(URL url) throws IllegalArgumentException {
      LOG.trace("enter HttpClient.startSession(String, int, Credentials, boolean)");
      int port = url.getPort();
      Protocol protocol = Protocol.getProtocol(url.getProtocol());
      this.hostConfiguration.setHost(url.getHost(), (String)null, port, protocol);
   }

   /** @deprecated */
   public void startSession(URL url, Credentials creds) throws IllegalArgumentException {
      LOG.trace("enter HttpClient.startSession(URL, Credentials)");
      this.getState().setCredentials((String)null, creds);
      this.startSession(url);
   }

   /** @deprecated */
   public void startSession(String host, int port, String proxyhost, int proxyport) {
      LOG.trace("enter HttpClient.startSession(String, int, String, int)");
      this.startSession(host, port, proxyhost, proxyport, false);
   }

   /** @deprecated */
   public void startSession(String host, int port, String proxyhost, int proxyport, boolean secure) {
      LOG.trace("enter HttpClient.startSession(String, int, String, int, boolean)");
      this.hostConfiguration.setHost(host, port, secure ? "https" : "http");
      this.hostConfiguration.setProxy(proxyhost, proxyport);
   }

   public int executeMethod(HttpMethod method) throws IOException, HttpException {
      LOG.trace("enter HttpClient.executeMethod(HttpMethod)");
      return this.executeMethod(method.getHostConfiguration() != null ? method.getHostConfiguration() : this.getHostConfiguration(), method, (HttpState)null);
   }

   public int executeMethod(HostConfiguration hostConfiguration, HttpMethod method) throws IOException, HttpException {
      LOG.trace("enter HttpClient.executeMethod(HostConfiguration,HttpMethod)");
      return this.executeMethod(hostConfiguration, method, (HttpState)null);
   }

   public int executeMethod(HostConfiguration hostConfiguration, HttpMethod method, HttpState state) throws IOException, HttpException {
      LOG.trace("enter HttpClient.executeMethod(HostConfiguration,HttpMethod,HttpState)");
      if (method == null) {
         throw new IllegalArgumentException("HttpMethod parameter may not be null");
      } else {
         int soTimeout = false;
         boolean strictMode = false;
         int connectionTimeout = false;
         long httpConnectionTimeout = 0L;
         HostConfiguration defaultHostConfiguration = null;
         int soTimeout;
         int connectionTimeout;
         synchronized(this) {
            soTimeout = this.timeoutInMilliseconds;
            strictMode = this.strictMode;
            connectionTimeout = this.connectionTimeout;
            httpConnectionTimeout = this.httpConnectionTimeout;
            if (state == null) {
               state = this.getState();
            }

            defaultHostConfiguration = this.getHostConfiguration();
         }

         HostConfiguration methodConfiguration = new HostConfiguration(hostConfiguration);
         if (hostConfiguration != defaultHostConfiguration) {
            if (!methodConfiguration.isHostSet()) {
               methodConfiguration.setHost(defaultHostConfiguration.getHost(), defaultHostConfiguration.getVirtualHost(), defaultHostConfiguration.getPort(), defaultHostConfiguration.getProtocol());
            }

            if (!methodConfiguration.isProxySet() && defaultHostConfiguration.isProxySet()) {
               methodConfiguration.setProxy(defaultHostConfiguration.getProxyHost(), defaultHostConfiguration.getProxyPort());
            }

            if (methodConfiguration.getLocalAddress() == null && defaultHostConfiguration.getLocalAddress() != null) {
               methodConfiguration.setLocalAddress(defaultHostConfiguration.getLocalAddress());
            }
         }

         HttpConnectionManager connmanager = this.httpConnectionManager;
         if (state.getHttpConnectionManager() != null) {
            connmanager = state.getHttpConnectionManager();
         }

         HttpConnection connection = connmanager.getConnection(methodConfiguration, httpConnectionTimeout);

         try {
            ((HttpMethod)method).setStrictMode(strictMode);
            if (!connection.isOpen()) {
               connection.setConnectionTimeout(connectionTimeout);
               connection.open();
               if (connection.isProxied() && connection.isSecure()) {
                  method = new ConnectMethod((HttpMethod)method);
               }
            }

            connection.setSoTimeout(soTimeout);
         } catch (IOException var16) {
            connection.releaseConnection();
            throw var16;
         } catch (RuntimeException var17) {
            connection.releaseConnection();
            throw var17;
         }

         return ((HttpMethod)method).execute(state, connection);
      }
   }

   /** @deprecated */
   public void endSession() throws IOException {
   }

   /** @deprecated */
   public String getHost() {
      return this.hostConfiguration.getHost();
   }

   /** @deprecated */
   public int getPort() {
      return this.hostConfiguration.getPort();
   }

   public synchronized HostConfiguration getHostConfiguration() {
      return this.hostConfiguration;
   }

   public synchronized void setHostConfiguration(HostConfiguration hostConfiguration) {
      this.hostConfiguration = hostConfiguration;
   }

   public synchronized HttpConnectionManager getHttpConnectionManager() {
      return this.httpConnectionManager;
   }

   public synchronized void setHttpConnectionManager(HttpConnectionManager httpConnectionManager) {
      this.httpConnectionManager = httpConnectionManager;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$HttpClient == null ? (class$org$apache$commons$httpclient$HttpClient = class$("org.apache.commons.httpclient.HttpClient")) : class$org$apache$commons$httpclient$HttpClient);
      if (LOG.isDebugEnabled()) {
         try {
            LOG.debug("Java version: " + System.getProperty("java.version"));
            LOG.debug("Java vendor: " + System.getProperty("java.vendor"));
            LOG.debug("Java class path: " + System.getProperty("java.class.path"));
            LOG.debug("Operating system name: " + System.getProperty("os.name"));
            LOG.debug("Operating system architecture: " + System.getProperty("os.arch"));
            LOG.debug("Operating system version: " + System.getProperty("os.version"));
            Provider[] providers = Security.getProviders();

            for(int i = 0; i < providers.length; ++i) {
               Provider provider = providers[i];
               LOG.debug(provider.getName() + " " + provider.getVersion() + ": " + provider.getInfo());
            }
         } catch (SecurityException var3) {
         }
      }

   }
}

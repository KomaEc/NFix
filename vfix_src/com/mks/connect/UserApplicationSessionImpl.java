package com.mks.connect;

import com.mks.api.CmdRunner;
import com.mks.api.IntegrationPoint;
import com.mks.api.IntegrationPointFactory;
import com.mks.api.Session;
import com.mks.api.VersionNumber;
import com.mks.api.response.APIConnectionException;
import com.mks.api.response.APIException;
import com.mks.api.response.APIInternalError;
import com.mks.api.util.Base64;
import com.mks.api.util.MKSLogger;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import javax.net.ssl.SSLSocket;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpRecoverableException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

final class UserApplicationSessionImpl extends CmdRunnerCreatorImpl implements Session {
   private static URI API_URI = null;
   private int timeout = 300000;
   private boolean autoReconnect;
   private static final String INVALID_API_URI_MSG = "API URI not initialized.";
   private static final String SET_TIMEOUT_MSG = "Setting connection timeout to: {0,number,#}";
   private static final String REDIRECT_FAILED_MSG = "Failed to establish a session: {0}";
   private static final String HEADER_NOT_FOUND_MSG = "Cannot get session ID.";
   private static final String BAD_STATUS_LINE_MSG = "Bad status line: {0}";
   private static final String AUTHENTICATION_FAILED_MSG = "Session not authenticated/authorized.";
   static final String API_COMMUNICATION_LOCALE = "UTF-8";
   private static final int DEFAULT_RETRIES = 3;
   private static final int DEFAULT_RETRY_SLEEP = 3000;
   private static final int RETRIES = Integer.getInteger("IntegrityAPI.retries", 3);
   private static final int RETRY_SLEEP = Integer.getInteger("IntegrityAPI.retryPeriod", 3000);
   protected static final Header OUT_OF_BAND_MESSAGE = new Header("OutOfBandMessage", "1");
   private static final Header PRE_9_6_PROTOCOL_VERSION = new Header("Protocol-version", "1.1");
   private static final Header PROTOCOL_VERSION = new Header("Protocol-version", "1.2");
   private static final Header SESSION_RELEASE = new Header("AppConnection", "close");
   private static final Header CODEPAGE = new Header("CodePage", "UTF-8");
   private static final Header NEW_SESSION = new Header("AppSession", "new");
   private static final Header TIMEZONE = new Header("TimeZone", TimeZone.getDefault().getID());
   private MKSLogger apiLogger;
   private URI url;
   private boolean anonymous;
   private IntegrationPoint ip;
   private HostConfiguration hostconfig = new HostConfiguration();
   private HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
   private String sessionUser;
   private String sessionPass;
   private Header sadCookie = null;
   private boolean supportsChunking;
   private VersionNumber apiVersion;

   UserApplicationSessionImpl(IntegrationPoint ip, VersionNumber apiRequestVersion, String username, String password, boolean anonymous) {
      if (API_URI == null) {
         throw new APIInternalError("API URI not initialized.");
      } else {
         this.ip = ip;
         this.apiVersion = apiRequestVersion;
         this.anonymous = anonymous;
         this.sessionUser = username;
         this.sessionPass = password;
         this.apiLogger = IntegrationPointFactory.getLogger();
         configureHttpClient(this.httpClient, this.hostconfig, ip);
         if (!ip.isClientIntegrationPoint()) {
            String ipHostname = ip.getHostname();
            if (!"localhost".equalsIgnoreCase(ipHostname) && !"127.0.0.1".equalsIgnoreCase(ipHostname)) {
               this.setDefaultHostname(ipHostname);
               this.setDefaultPort(ip.getPort());
            }
         }

         if (this.sessionUser != null) {
            this.setDefaultUsername(this.sessionUser);
         }

         if (this.sessionPass != null) {
            this.setDefaultPassword(this.sessionPass);
         }

      }
   }

   protected HttpClient createHttpClient() {
      HttpClient retval = new HttpClient(new MultiThreadedHttpConnectionManager());
      configureHttpClient(retval, this.hostconfig);
      return retval;
   }

   protected static void releaseHttpClient(HttpClient client) {
      HttpConnectionManager hcm = client.getHttpConnectionManager();
      if (hcm instanceof MultiThreadedHttpConnectionManager) {
         ((MultiThreadedHttpConnectionManager)hcm).shutdown();
      }

   }

   protected static void configureHttpClient(HttpClient httpClient, HostConfiguration hostConfig) {
      configureHttpClient(httpClient, hostConfig, (IntegrationPoint)null);
   }

   protected static void configureHttpClient(HttpClient httpClient, HostConfiguration hostConfig, IntegrationPoint ip) {
      if (ip != null) {
         Protocol protocol = ip.isSecure() ? new Protocol("https", new UserApplicationSessionImpl.SSLSocketFactory(), 443) : Protocol.getProtocol("http");
         hostConfig.setHost(ip.getHostname(), ip.getPort(), protocol);
      }

      httpClient.setHostConfiguration(hostConfig);
   }

   protected static void handleHTTPResponse(HttpClient httpClient, HttpMethod method) throws IOException {
      int retry = 0;

      while(true) {
         try {
            httpClient.executeMethod(method);
            return;
         } catch (HttpRecoverableException var6) {
            IntegrationPointFactory.getLogger().message("API", 5, "Got recoverable exception: " + var6.getLocalizedMessage() + ", sleeping for: " + RETRY_SLEEP + "ms, then retrying...");
            ++retry;
            if (retry >= RETRIES) {
               throw var6;
            }

            try {
               Thread.sleep((long)RETRY_SLEEP);
            } catch (InterruptedException var5) {
            }
         }
      }
   }

   protected synchronized URI getSession(URI apiURL) throws IOException {
      HeadMethod method = new HeadMethod(apiURL.getPath());

      URI var7;
      try {
         method.setFollowRedirects(false);
         this.setupRequest(method);
         method.setQueryString(apiURL.getQuery());
         if (!this.anonymous) {
            method.setRequestHeader(NEW_SESSION);
         }

         String msg = MessageFormat.format("Setting connection timeout to: {0,number,#}", new Integer(this.timeout));
         this.apiLogger.message((Object)this, "API", 10, msg);
         this.httpClient.setTimeout(this.timeout);
         handleHTTPResponse(this.httpClient, method);
         Header server = method.getResponseHeader("Server");
         this.supportsChunking = server == null || !server.getValue().startsWith("WebLogic 5.1.0");
         int code = method.getStatusCode();
         if (code == 410) {
            this.invalidateURI();
            throw new InvalidSessionException();
         }

         String errMsg;
         if (code != 302) {
            String errDetails = method.getStatusText();
            if (code == 403) {
               errDetails = "Session not authenticated/authorized.";
            }

            errMsg = MessageFormat.format("Failed to establish a session: {0}", errDetails);
            throw new BlimpException(errMsg);
         }

         Header red = method.getResponseHeader("Location");
         if (red == null) {
            errMsg = "Cannot get session ID.";
            throw new IOException(errMsg);
         }

         var7 = new URI(red.getValue());
      } finally {
         method.releaseConnection();
      }

      return var7;
   }

   public int getTimeout() {
      return this.timeout / 1000;
   }

   public void setTimeout(int timeout) {
      this.timeout = timeout * 1000;
   }

   protected void removeConnection(CmdRunner c) {
      this.removeCmdRunner(c);
   }

   protected synchronized URI getSessionURI() throws IOException {
      if (this.url == null) {
         this.url = this.getSession(API_URI);
      }

      return this.url;
   }

   protected synchronized void invalidateURI() {
      this.url = null;
   }

   public IntegrationPoint getIntegrationPoint() {
      return this.ip;
   }

   public VersionNumber getAPIRequestVersion() {
      return this.apiVersion == null ? this.ip.getAPIRequestVersion() : this.apiVersion;
   }

   protected void setupRequest(HttpMethod method) {
      method.setRequestHeader(PRE_9_6_PROTOCOL_VERSION);
      method.setRequestHeader(CODEPAGE);
      method.setRequestHeader(TIMEZONE);
      if (this.sadCookie != null) {
         method.setRequestHeader(this.sadCookie);
      }

      if (this.sessionUser != null) {
         method.setRequestHeader("Authorization", "Basic " + Base64.encode(this.sessionUser + ":" + this.sessionPass));
      }

      String apiVersion = this.getAPIRequestVersion().toVersionString();
      if (apiVersion != null) {
         method.setRequestHeader(new Header("APIVersion", apiVersion));
      } else {
         this.apiLogger.message((Object)this, "ERROR", 0, "API version not available!");
      }

   }

   public final void release(boolean force) throws IOException, APIException {
      this.release(force, true);
   }

   protected void release(boolean force, boolean removeParent) throws IOException, APIException {
      synchronized(this) {
         super.release(force);

         try {
            URI session = this.url;
            if (session != null) {
               HeadMethod method = new HeadMethod(session.getPath());

               try {
                  method.setQueryString(session.getQuery());
                  method.setFollowRedirects(false);
                  this.setupRequest(method);
                  method.setRequestHeader(SESSION_RELEASE);
                  handleHTTPResponse(this.httpClient, method);
                  int code = method.getStatusCode();
                  if (code != 200 && code != 302 && code != 410) {
                     String msg = MessageFormat.format("Bad status line: {0}", method.getStatusLine());
                     throw new BlimpException(msg);
                  }
               } finally {
                  method.releaseConnection();
               }
            }
         } catch (ConnectException var20) {
         } finally {
            releaseHttpClient(this.httpClient);
         }

         this.invalidateURI();
      }

      if (removeParent) {
         ((IntegrationPointImpl)this.ip).removeSession(this);
      }

   }

   protected CmdRunner _createCmdRunner() throws APIException {
      Object cr = null;

      try {
         if (this.ip.isClientIntegrationPoint()) {
            cr = new ClientCmdRunnerImpl(this, this.createHttpClient());
            configureHttpClient(this.httpClient, this.hostconfig, this.ip);
         } else {
            cr = new HttpCmdRunnerImpl(this, this.createHttpClient());
         }

         ((CmdRunner)cr).setDefaultUsername(this.sessionUser);
         ((CmdRunner)cr).setDefaultPassword(this.sessionPass);
      } catch (UnsatisfiedLinkError var3) {
         this.apiLogger.exception((Object)this, "API", 0, var3);
         if (!System.getProperty("os.name").startsWith("Windows")) {
            throw new APIConnectionException(var3);
         }

         ((IntegrationPointImpl)this.ip).setPort(31000);
         configureHttpClient(this.httpClient, this.hostconfig, this.ip);
         cr = new HttpCmdRunnerImpl(this, this.createHttpClient());
      }

      return (CmdRunner)cr;
   }

   public boolean isCommon() {
      return this.anonymous;
   }

   protected void setAuthenticationCookie(String cookie) {
      this.sadCookie = null;
      if (cookie != null) {
         this.sadCookie = new Header("SadCookie", cookie);
      }

   }

   public void setAutoReconnect(boolean autoReconnect) {
      this.autoReconnect = autoReconnect;
   }

   public boolean getAutoReconnect() {
      return this.autoReconnect;
   }

   protected boolean supportsChunking() {
      return this.supportsChunking;
   }

   static {
      try {
         API_URI = new URI("/icapi");
      } catch (URIException var1) {
         IntegrationPointFactory.getLogger().exception((Class)UserApplicationSessionImpl.class, "API", 0, var1);
         var1.printStackTrace();
      }

      System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
      System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
      boolean debugHTTP = Boolean.getBoolean("IntegrityAPI.log.HTTP");
      if (debugHTTP) {
         IntegrationPointFactory.getLogger().message("API", "Logging http from HTTPClient");
         System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
         System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");
      } else {
         System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "off");
         System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "off");
      }

   }

   private static class SSLSocketFactory implements SecureProtocolSocketFactory {
      private final SecureProtocolSocketFactory delegate = (SecureProtocolSocketFactory)Protocol.getProtocol("https").getSocketFactory();

      public SSLSocketFactory() {
      }

      public Socket createSocket(String host, int port) throws IOException {
         Socket socket = this.delegate.createSocket(host, port);
         this.configureSocket(socket);
         return socket;
      }

      public Socket createSocket(String host, int port, InetAddress localAddress, int localPort) throws IOException {
         Socket socket = this.delegate.createSocket(host, port, localAddress, localPort);
         this.configureSocket(socket);
         return socket;
      }

      public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
         Socket newSocket = this.delegate.createSocket(socket, host, port, autoClose);
         this.configureSocket(newSocket);
         return newSocket;
      }

      private void configureSocket(Socket socket) {
         SSLSocket sslSocket = (SSLSocket)socket;
         List protocols = new ArrayList(Arrays.asList(sslSocket.getEnabledProtocols()));
         protocols.remove("SSLv2Hello");
         sslSocket.setEnabledProtocols((String[])protocols.toArray(new String[protocols.size()]));
      }
   }
}

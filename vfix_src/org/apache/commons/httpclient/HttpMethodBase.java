package org.apache.commons.httpclient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.commons.httpclient.auth.HttpAuthenticator;
import org.apache.commons.httpclient.auth.MalformedChallengeException;
import org.apache.commons.httpclient.auth.NTLMScheme;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.httpclient.cookie.MalformedCookieException;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class HttpMethodBase implements HttpMethod {
   private static final int MAX_FORWARDS = 100;
   private static final Log LOG;
   protected static final Header USER_AGENT;
   private HeaderGroup requestHeaders = new HeaderGroup();
   private StatusLine statusLine = null;
   private HeaderGroup responseHeaders = new HeaderGroup();
   private HeaderGroup responseTrailerHeaders = new HeaderGroup();
   private AuthScheme authScheme = null;
   private Set realms = null;
   private String realm = null;
   private AuthScheme proxyAuthScheme = null;
   private Set proxyRealms = null;
   private String proxyRealm = null;
   private String path = null;
   private String queryString = null;
   private InputStream responseStream = null;
   private HttpConnection responseConnection = null;
   private byte[] responseBody = null;
   private boolean followRedirects = false;
   private boolean doAuthentication = true;
   private boolean http11 = true;
   private boolean strictMode = false;
   private boolean used = false;
   private int recoverableExceptionCount = 0;
   private HostConfiguration hostConfiguration;
   private MethodRetryHandler methodRetryHandler;
   private boolean inExecute = false;
   private boolean doneWithConnection = false;
   private boolean connectionCloseForced = false;
   private static final int RESPONSE_WAIT_TIME_MS = 3000;
   private static final int BUFFER_WARN_TRIGGER_LIMIT = 1048576;
   private static final int DEFAULT_INITIAL_BUFFER_SIZE = 4096;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$HttpMethodBase;

   public HttpMethodBase() {
   }

   public HttpMethodBase(String uri) throws IllegalArgumentException, IllegalStateException {
      try {
         if (uri == null || uri.equals("")) {
            uri = "/";
         }

         URI parsedURI = new URI(uri.toCharArray());
         if (parsedURI.isAbsoluteURI()) {
            this.hostConfiguration = new HostConfiguration();
            this.hostConfiguration.setHost(parsedURI.getHost(), parsedURI.getPort(), parsedURI.getScheme());
         }

         this.setPath(parsedURI.getPath() == null ? "/" : parsedURI.getEscapedPath());
         this.setQueryString(parsedURI.getEscapedQuery());
      } catch (URIException var3) {
         throw new IllegalArgumentException("Invalid uri '" + uri + "': " + var3.getMessage());
      }
   }

   public abstract String getName();

   public URI getURI() throws URIException {
      if (this.hostConfiguration == null) {
         URI tmpUri = new URI((String)null, (String)null, this.path, (String)null, (String)null);
         tmpUri.setEscapedQuery(this.queryString);
         return tmpUri;
      } else {
         int port = this.hostConfiguration.getPort();
         if (port == this.hostConfiguration.getProtocol().getDefaultPort()) {
            port = -1;
         }

         URI tmpUri = new URI(this.hostConfiguration.getProtocol().getScheme(), (String)null, this.hostConfiguration.getHost(), port, this.path, (String)null);
         tmpUri.setEscapedQuery(this.queryString);
         return tmpUri;
      }
   }

   public void setFollowRedirects(boolean followRedirects) {
      this.followRedirects = followRedirects;
   }

   public boolean getFollowRedirects() {
      return this.followRedirects;
   }

   public void setHttp11(boolean http11) {
      this.http11 = http11;
   }

   public boolean getDoAuthentication() {
      return this.doAuthentication;
   }

   public void setDoAuthentication(boolean doAuthentication) {
      this.doAuthentication = doAuthentication;
   }

   public boolean isHttp11() {
      return this.http11;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public void addRequestHeader(Header header) {
      LOG.trace("HttpMethodBase.addRequestHeader(Header)");
      if (header == null) {
         LOG.debug("null header value ignored");
      } else {
         this.getRequestHeaderGroup().addHeader(header);
      }

   }

   public void addResponseFooter(Header footer) {
      this.getResponseTrailerHeaderGroup().addHeader(footer);
   }

   public String getPath() {
      return this.path != null && !this.path.equals("") ? this.path : "/";
   }

   public void setQueryString(String queryString) {
      this.queryString = queryString;
   }

   public void setQueryString(NameValuePair[] params) {
      LOG.trace("enter HttpMethodBase.setQueryString(NameValuePair[])");
      this.queryString = EncodingUtil.formUrlEncode(params, "UTF-8");
   }

   public String getQueryString() {
      return this.queryString;
   }

   public void setRequestHeader(String headerName, String headerValue) {
      Header header = new Header(headerName, headerValue);
      this.setRequestHeader(header);
   }

   public void setRequestHeader(Header header) {
      Header[] headers = this.getRequestHeaderGroup().getHeaders(header.getName());

      for(int i = 0; i < headers.length; ++i) {
         this.getRequestHeaderGroup().removeHeader(headers[i]);
      }

      this.getRequestHeaderGroup().addHeader(header);
   }

   public Header getRequestHeader(String headerName) {
      return headerName == null ? null : this.getRequestHeaderGroup().getCondensedHeader(headerName);
   }

   public Header[] getRequestHeaders() {
      return this.getRequestHeaderGroup().getAllHeaders();
   }

   protected HeaderGroup getRequestHeaderGroup() {
      return this.requestHeaders;
   }

   protected HeaderGroup getResponseTrailerHeaderGroup() {
      return this.responseTrailerHeaders;
   }

   protected HeaderGroup getResponseHeaderGroup() {
      return this.responseHeaders;
   }

   public int getStatusCode() {
      return this.statusLine.getStatusCode();
   }

   public StatusLine getStatusLine() {
      return this.statusLine;
   }

   private boolean responseAvailable() {
      return this.responseBody != null || this.responseStream != null;
   }

   public Header[] getResponseHeaders() {
      return this.getResponseHeaderGroup().getAllHeaders();
   }

   public Header getResponseHeader(String headerName) {
      return headerName == null ? null : this.getResponseHeaderGroup().getCondensedHeader(headerName);
   }

   protected int getResponseContentLength() {
      Header[] headers = this.getResponseHeaderGroup().getHeaders("Content-Length");
      if (headers.length == 0) {
         return -1;
      } else {
         if (headers.length > 1) {
            LOG.warn("Multiple content-length headers detected");
         }

         int i = headers.length - 1;

         while(i >= 0) {
            Header header = headers[i];

            try {
               return Integer.parseInt(header.getValue());
            } catch (NumberFormatException var5) {
               if (LOG.isWarnEnabled()) {
                  LOG.warn("Invalid content-length value: " + var5.getMessage());
               }

               --i;
            }
         }

         return -1;
      }
   }

   public byte[] getResponseBody() {
      if (this.responseBody == null) {
         try {
            InputStream instream = this.getResponseBodyAsStream();
            if (instream != null) {
               int contentLength = this.getResponseContentLength();
               if (contentLength == -1 || contentLength > 1048576) {
                  LOG.warn("Going to buffer response body of large or unknown size. Using getResponseAsStream instead is recommended.");
               }

               LOG.debug("Buffering response body");
               ByteArrayOutputStream outstream = new ByteArrayOutputStream(contentLength > 0 ? contentLength : 4096);
               byte[] buffer = new byte[4096];

               int len;
               while((len = instream.read(buffer)) > 0) {
                  outstream.write(buffer, 0, len);
               }

               outstream.close();
               this.setResponseStream((InputStream)null);
               this.responseBody = outstream.toByteArray();
            }
         } catch (IOException var6) {
            LOG.error("I/O failure reading response body", var6);
            this.responseBody = null;
         }
      }

      return this.responseBody;
   }

   public InputStream getResponseBodyAsStream() throws IOException {
      if (this.responseStream != null) {
         return this.responseStream;
      } else if (this.responseBody != null) {
         InputStream byteResponseStream = new ByteArrayInputStream(this.responseBody);
         LOG.debug("re-creating response stream from byte array");
         return byteResponseStream;
      } else {
         return null;
      }
   }

   public String getResponseBodyAsString() {
      byte[] rawdata = null;
      if (this.responseAvailable()) {
         rawdata = this.getResponseBody();
      }

      return rawdata != null ? HttpConstants.getContentString(rawdata, this.getResponseCharSet()) : null;
   }

   public Header[] getResponseFooters() {
      return this.getResponseTrailerHeaderGroup().getAllHeaders();
   }

   public Header getResponseFooter(String footerName) {
      return footerName == null ? null : this.getResponseTrailerHeaderGroup().getCondensedHeader(footerName);
   }

   protected void setResponseStream(InputStream responseStream) {
      this.responseStream = responseStream;
   }

   protected InputStream getResponseStream() {
      return this.responseStream;
   }

   public String getStatusText() {
      return this.statusLine.getReasonPhrase();
   }

   public void setStrictMode(boolean strictMode) {
      this.strictMode = strictMode;
   }

   public boolean isStrictMode() {
      return this.strictMode;
   }

   public void addRequestHeader(String headerName, String headerValue) {
      this.addRequestHeader(new Header(headerName, headerValue));
   }

   protected boolean isConnectionCloseForced() {
      return this.connectionCloseForced;
   }

   protected void setConnectionCloseForced(boolean b) {
      if (LOG.isDebugEnabled()) {
         LOG.debug("Force-close connection: " + b);
      }

      this.connectionCloseForced = b;
   }

   protected boolean shouldCloseConnection(HttpConnection conn) {
      if (this.isConnectionCloseForced()) {
         LOG.debug("Should force-close connection.");
         return true;
      } else {
         Header connectionHeader = null;
         if (!conn.isTransparent()) {
            connectionHeader = this.responseHeaders.getFirstHeader("proxy-connection");
         }

         if (connectionHeader == null) {
            connectionHeader = this.responseHeaders.getFirstHeader("connection");
         }

         if (connectionHeader != null) {
            if (connectionHeader.getValue().equalsIgnoreCase("close")) {
               if (LOG.isDebugEnabled()) {
                  LOG.debug("Should close connection in response to " + connectionHeader.toExternalForm());
               }

               return true;
            }

            if (connectionHeader.getValue().equalsIgnoreCase("keep-alive")) {
               if (LOG.isDebugEnabled()) {
                  LOG.debug("Should NOT close connection in response to " + connectionHeader.toExternalForm());
               }

               return false;
            }

            if (LOG.isDebugEnabled()) {
               LOG.debug("Unknown directive: " + connectionHeader.toExternalForm());
            }
         }

         LOG.debug("Resorting to protocol version default close connection policy");
         if (this.http11) {
            LOG.debug("Should NOT close connection, using HTTP/1.1.");
         } else {
            LOG.debug("Should close connection, using HTTP/1.0.");
         }

         return !this.http11;
      }
   }

   private boolean isRetryNeeded(int statusCode, HttpState state, HttpConnection conn) {
      switch(statusCode) {
      case 301:
      case 302:
      case 303:
      case 307:
         LOG.debug("Redirect required");
         if (!this.processRedirectResponse(conn)) {
            return false;
         }
         break;
      case 401:
      case 407:
         LOG.debug("Authorization required");
         if (!this.doAuthentication) {
            return false;
         }

         if (this.processAuthenticationResponse(state, conn)) {
            return false;
         }
         break;
      default:
         return false;
      }

      return true;
   }

   private void checkExecuteConditions(HttpState state, HttpConnection conn) throws HttpException {
      if (state == null) {
         throw new IllegalArgumentException("HttpState parameter may not be null");
      } else if (conn == null) {
         throw new IllegalArgumentException("HttpConnection parameter may not be null");
      } else if (this.hasBeenUsed()) {
         throw new HttpException("Already used, but not recycled.");
      } else if (!this.validate()) {
         throw new HttpException("Not valid");
      } else if (this.inExecute) {
         throw new IllegalStateException("Execute invoked recursively, or exited abnormally.");
      }
   }

   public int execute(HttpState state, HttpConnection conn) throws HttpException, HttpRecoverableException, IOException {
      LOG.trace("enter HttpMethodBase.execute(HttpState, HttpConnection)");
      this.responseConnection = conn;
      this.checkExecuteConditions(state, conn);
      this.inExecute = true;

      try {
         if (state.isAuthenticationPreemptive()) {
            LOG.debug("Preemptively sending default basic credentials");

            try {
               if (HttpAuthenticator.authenticateDefault(this, conn, state)) {
                  LOG.debug("Default basic credentials applied");
               } else {
                  LOG.warn("Preemptive authentication failed");
               }

               if (conn.isProxied()) {
                  if (HttpAuthenticator.authenticateProxyDefault(this, conn, state)) {
                     LOG.debug("Default basic proxy credentials applied");
                  } else {
                     LOG.warn("Preemptive proxy authentication failed");
                  }
               }
            } catch (AuthenticationException var7) {
               LOG.error(var7.getMessage(), var7);
            }
         }

         this.realms = new HashSet();
         this.proxyRealms = new HashSet();
         int forwardCount = 0;

         while(forwardCount++ < 100) {
            conn.setLastResponseInputStream((InputStream)null);
            if (LOG.isDebugEnabled()) {
               LOG.debug("Execute loop try " + forwardCount);
            }

            this.statusLine = null;
            this.connectionCloseForced = false;
            this.processRequest(state, conn);
            if (!this.isRetryNeeded(this.statusLine.getStatusCode(), state, conn)) {
               break;
            }

            if (this.responseStream != null) {
               this.responseStream.close();
            }
         }

         if (forwardCount >= 100) {
            LOG.error("Narrowly avoided an infinite loop in execute");
            throw new HttpRecoverableException("Maximum redirects (100) exceeded");
         }
      } finally {
         this.inExecute = false;
         if (this.doneWithConnection) {
            this.ensureConnectionRelease();
         }

      }

      return this.statusLine.getStatusCode();
   }

   private boolean processRedirectResponse(HttpConnection conn) {
      if (!this.getFollowRedirects()) {
         LOG.info("Redirect requested but followRedirects is disabled");
         return false;
      } else {
         Header locationHeader = this.getResponseHeader("location");
         if (locationHeader == null) {
            LOG.error("Received redirect response " + this.getStatusCode() + " but no location header");
            return false;
         } else {
            String location = locationHeader.getValue();
            if (LOG.isDebugEnabled()) {
               LOG.debug("Redirect requested to location '" + location + "'");
            }

            URI redirectUri = null;
            URI currentUri = null;

            try {
               currentUri = new URI(conn.getProtocol().getScheme(), (String)null, conn.getHost(), conn.getPort(), this.getPath());
               redirectUri = new URI(location.toCharArray());
               if (redirectUri.isRelativeURI()) {
                  if (this.isStrictMode()) {
                     LOG.warn("Redirected location '" + location + "' is not acceptable in strict mode");
                     return false;
                  }

                  LOG.debug("Redirect URI is not absolute - parsing as relative");
                  redirectUri = new URI(currentUri, redirectUri);
               }
            } catch (URIException var8) {
               LOG.warn("Redirected location '" + location + "' is malformed");
               return false;
            }

            try {
               checkValidRedirect(currentUri, redirectUri);
            } catch (HttpException var7) {
               LOG.warn(var7.getMessage());
               return false;
            }

            this.realms.clear();
            if (this.proxyAuthScheme instanceof NTLMScheme) {
               this.removeRequestHeader("Proxy-Authorization");
            }

            this.removeRequestHeader("Authorization");
            this.setPath(redirectUri.getEscapedPath());
            this.setQueryString(redirectUri.getEscapedQuery());
            if (LOG.isDebugEnabled()) {
               LOG.debug("Redirecting from '" + currentUri.getEscapedURI() + "' to '" + redirectUri.getEscapedURI());
            }

            return true;
         }
      }
   }

   private static void checkValidRedirect(URI currentUri, URI redirectUri) throws HttpException {
      LOG.trace("enter HttpMethodBase.checkValidRedirect(HttpConnection, URL)");
      String oldProtocol = currentUri.getScheme();
      String newProtocol = redirectUri.getScheme();
      if (!oldProtocol.equals(newProtocol)) {
         throw new HttpException("Redirect from protocol " + oldProtocol + " to " + newProtocol + " is not supported");
      } else {
         try {
            String oldHost = currentUri.getHost();
            String newHost = redirectUri.getHost();
            if (!oldHost.equalsIgnoreCase(newHost)) {
               throw new HttpException("Redirect from host " + oldHost + " to " + newHost + " is not supported");
            }
         } catch (URIException var6) {
            LOG.warn("Error getting URI host", var6);
            throw new HttpException("Invalid Redirect URI from: " + currentUri.getEscapedURI() + " to: " + redirectUri.getEscapedURI());
         }

         int oldPort = currentUri.getPort();
         if (oldPort < 0) {
            oldPort = getDefaultPort(oldProtocol);
         }

         int newPort = redirectUri.getPort();
         if (newPort < 0) {
            newPort = getDefaultPort(newProtocol);
         }

         if (oldPort != newPort) {
            throw new HttpException("Redirect from port " + oldPort + " to " + newPort + " is not supported");
         }
      }
   }

   private static int getDefaultPort(String protocol) {
      String proto = protocol.toLowerCase().trim();
      if (proto.equals("http")) {
         return 80;
      } else {
         return proto.equals("https") ? 443 : -1;
      }
   }

   public boolean hasBeenUsed() {
      return this.used;
   }

   /** @deprecated */
   public void recycle() {
      LOG.trace("enter HttpMethodBase.recycle()");
      this.releaseConnection();
      this.path = null;
      this.followRedirects = false;
      this.doAuthentication = true;
      this.authScheme = null;
      this.realm = null;
      this.proxyAuthScheme = null;
      this.proxyRealm = null;
      this.queryString = null;
      this.getRequestHeaderGroup().clear();
      this.getResponseHeaderGroup().clear();
      this.getResponseTrailerHeaderGroup().clear();
      this.statusLine = null;
      this.used = false;
      this.http11 = true;
      this.responseBody = null;
      this.recoverableExceptionCount = 0;
      this.inExecute = false;
      this.doneWithConnection = false;
      this.connectionCloseForced = false;
   }

   public void releaseConnection() {
      if (this.responseStream != null) {
         try {
            this.responseStream.close();
         } catch (IOException var2) {
            this.ensureConnectionRelease();
         }
      } else {
         this.ensureConnectionRelease();
      }

   }

   public void removeRequestHeader(String headerName) {
      Header[] headers = this.getRequestHeaderGroup().getHeaders(headerName);

      for(int i = 0; i < headers.length; ++i) {
         this.getRequestHeaderGroup().removeHeader(headers[i]);
      }

   }

   public boolean validate() {
      return true;
   }

   protected int getRequestContentLength() {
      return 0;
   }

   protected void addAuthorizationRequestHeader(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter HttpMethodBase.addAuthorizationRequestHeader(HttpState, HttpConnection)");
      if (this.getRequestHeader("Authorization") == null) {
         Header[] challenges = this.getResponseHeaderGroup().getHeaders("WWW-Authenticate");
         if (challenges.length > 0) {
            try {
               this.authScheme = HttpAuthenticator.selectAuthScheme(challenges);
               HttpAuthenticator.authenticate(this.authScheme, this, conn, state);
            } catch (HttpException var5) {
               if (LOG.isErrorEnabled()) {
                  LOG.error(var5.getMessage(), var5);
               }
            }
         }
      }

   }

   protected void addContentLengthRequestHeader(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter HttpMethodBase.addContentLengthRequestHeader(HttpState, HttpConnection)");
      int len = this.getRequestContentLength();
      if (this.getRequestHeader("content-length") == null) {
         if (0 < len) {
            this.setRequestHeader("Content-Length", String.valueOf(len));
         } else if (this.http11 && len < 0) {
            this.setRequestHeader("Transfer-Encoding", "chunked");
         }
      }

   }

   protected void addCookieRequestHeader(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter HttpMethodBase.addCookieRequestHeader(HttpState, HttpConnection)");
      this.removeRequestHeader("cookie");
      CookieSpec matcher = CookiePolicy.getSpecByPolicy(state.getCookiePolicy());
      Cookie[] cookies = matcher.match(conn.getHost(), conn.getPort(), this.getPath(), conn.isSecure(), state.getCookies());
      if (cookies != null && cookies.length > 0) {
         if (this.isStrictMode()) {
            this.getRequestHeaderGroup().addHeader(matcher.formatCookieHeader(cookies));
         } else {
            for(int i = 0; i < cookies.length; ++i) {
               this.getRequestHeaderGroup().addHeader(matcher.formatCookieHeader(cookies[i]));
            }
         }
      }

   }

   protected void addHostRequestHeader(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter HttpMethodBase.addHostRequestHeader(HttpState, HttpConnection)");
      String host = conn.getVirtualHost();
      if (host != null) {
         LOG.debug("Using virtual host name: " + host);
      } else {
         host = conn.getHost();
      }

      int port = conn.getPort();
      if (this.getRequestHeader("host") != null) {
         LOG.debug("Request to add Host header ignored: header already added");
      } else {
         if (LOG.isDebugEnabled()) {
            LOG.debug("Adding Host request header");
         }

         if (conn.getProtocol().getDefaultPort() != port) {
            host = host + ":" + port;
         }

         this.setRequestHeader("Host", host);
      }
   }

   protected void addProxyAuthorizationRequestHeader(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter HttpMethodBase.addProxyAuthorizationRequestHeader(HttpState, HttpConnection)");
      if (this.getRequestHeader("Proxy-Authorization") == null) {
         Header[] challenges = this.getResponseHeaderGroup().getHeaders("Proxy-Authenticate");
         if (challenges.length > 0) {
            try {
               this.proxyAuthScheme = HttpAuthenticator.selectAuthScheme(challenges);
               HttpAuthenticator.authenticateProxy(this.proxyAuthScheme, this, conn, state);
            } catch (HttpException var5) {
               if (LOG.isErrorEnabled()) {
                  LOG.error(var5.getMessage(), var5);
               }
            }
         }
      }

   }

   protected void addProxyConnectionHeader(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter HttpMethodBase.addProxyConnectionHeader(HttpState, HttpConnection)");
      if (!conn.isTransparent()) {
         this.setRequestHeader("Proxy-Connection", "Keep-Alive");
      }

   }

   protected void addRequestHeaders(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter HttpMethodBase.addRequestHeaders(HttpState, HttpConnection)");
      this.addUserAgentRequestHeader(state, conn);
      this.addHostRequestHeader(state, conn);
      this.addCookieRequestHeader(state, conn);
      this.addAuthorizationRequestHeader(state, conn);
      this.addProxyAuthorizationRequestHeader(state, conn);
      this.addProxyConnectionHeader(state, conn);
      this.addContentLengthRequestHeader(state, conn);
   }

   protected void addUserAgentRequestHeader(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter HttpMethodBase.addUserAgentRequestHeaders(HttpState, HttpConnection)");
      if (this.getRequestHeader("user-agent") == null) {
         this.setRequestHeader(USER_AGENT);
      }

   }

   protected void checkNotUsed() throws IllegalStateException {
      if (this.used) {
         throw new IllegalStateException("Already used.");
      }
   }

   protected void checkUsed() throws IllegalStateException {
      if (!this.used) {
         throw new IllegalStateException("Not Used.");
      }
   }

   protected static String generateRequestLine(HttpConnection connection, String name, String requestPath, String query, String version) {
      LOG.trace("enter HttpMethodBase.generateRequestLine(HttpConnection, String, String, String, String)");
      StringBuffer buf = new StringBuffer();
      buf.append(name);
      buf.append(" ");
      if (!connection.isTransparent()) {
         Protocol protocol = connection.getProtocol();
         buf.append(protocol.getScheme().toLowerCase());
         buf.append("://");
         buf.append(connection.getHost());
         if (connection.getPort() != -1 && connection.getPort() != protocol.getDefaultPort()) {
            buf.append(":");
            buf.append(connection.getPort());
         }
      }

      if (requestPath == null) {
         buf.append("/");
      } else {
         if (!connection.isTransparent() && !requestPath.startsWith("/")) {
            buf.append("/");
         }

         buf.append(requestPath);
      }

      if (query != null) {
         if (query.indexOf("?") != 0) {
            buf.append("?");
         }

         buf.append(query);
      }

      buf.append(" ");
      buf.append(version);
      buf.append("\r\n");
      return buf.toString();
   }

   protected void processResponseBody(HttpState state, HttpConnection conn) {
   }

   protected void processResponseHeaders(HttpState state, HttpConnection conn) {
      LOG.trace("enter HttpMethodBase.processResponseHeaders(HttpState, HttpConnection)");
      Header[] headers = this.getResponseHeaderGroup().getHeaders("set-cookie2");
      if (headers.length == 0) {
         headers = this.getResponseHeaderGroup().getHeaders("set-cookie");
      }

      CookieSpec parser = CookiePolicy.getSpecByPolicy(state.getCookiePolicy());

      for(int i = 0; i < headers.length; ++i) {
         Header header = headers[i];
         Cookie[] cookies = null;

         try {
            cookies = parser.parse(conn.getHost(), conn.getPort(), this.getPath(), conn.isSecure(), header);
         } catch (MalformedCookieException var12) {
            if (LOG.isWarnEnabled()) {
               LOG.warn("Invalid cookie header: \"" + header.getValue() + "\". " + var12.getMessage());
            }
         }

         if (cookies != null) {
            for(int j = 0; j < cookies.length; ++j) {
               Cookie cookie = cookies[j];

               try {
                  parser.validate(conn.getHost(), conn.getPort(), this.getPath(), conn.isSecure(), cookie);
                  state.addCookie(cookie);
                  if (LOG.isDebugEnabled()) {
                     LOG.debug("Cookie accepted: \"" + parser.formatCookie(cookie) + "\"");
                  }
               } catch (MalformedCookieException var11) {
                  if (LOG.isWarnEnabled()) {
                     LOG.warn("Cookie rejected: \"" + parser.formatCookie(cookie) + "\". " + var11.getMessage());
                  }
               }
            }
         }
      }

   }

   protected void processStatusLine(HttpState state, HttpConnection conn) {
   }

   protected void readResponse(HttpState state, HttpConnection conn) throws HttpException {
      LOG.trace("enter HttpMethodBase.readResponse(HttpState, HttpConnection)");

      try {
         while(this.statusLine == null) {
            this.readStatusLine(state, conn);
            this.processStatusLine(state, conn);
            this.readResponseHeaders(state, conn);
            this.processResponseHeaders(state, conn);
            int status = this.statusLine.getStatusCode();
            if (status >= 100 && status < 200) {
               if (LOG.isInfoEnabled()) {
                  LOG.info("Discarding unexpected response: " + this.statusLine.toString());
               }

               this.statusLine = null;
            }
         }

         this.readResponseBody(state, conn);
         this.processResponseBody(state, conn);
      } catch (IOException var4) {
         throw new HttpRecoverableException(var4.toString());
      }
   }

   protected void readResponseBody(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter HttpMethodBase.readResponseBody(HttpState, HttpConnection)");
      this.doneWithConnection = false;
      InputStream stream = this.readResponseBody(conn);
      if (stream == null) {
         this.responseBodyConsumed();
      } else {
         conn.setLastResponseInputStream(stream);
         this.setResponseStream(stream);
      }

   }

   private InputStream readResponseBody(HttpConnection conn) throws IOException {
      LOG.trace("enter HttpMethodBase.readResponseBody(HttpConnection)");
      this.responseBody = null;
      InputStream is = conn.getResponseInputStream();
      if (Wire.CONTENT_WIRE.enabled()) {
         is = new WireLogInputStream((InputStream)is, Wire.CONTENT_WIRE);
      }

      InputStream result = null;
      Header transferEncodingHeader = this.responseHeaders.getFirstHeader("Transfer-Encoding");
      if (transferEncodingHeader != null) {
         String transferEncoding = transferEncodingHeader.getValue();
         if (!"chunked".equalsIgnoreCase(transferEncoding) && !"identity".equalsIgnoreCase(transferEncoding) && LOG.isWarnEnabled()) {
            LOG.warn("Unsupported transfer encoding: " + transferEncoding);
         }

         HeaderElement[] encodings = transferEncodingHeader.getValues();
         int len = encodings.length;
         if (len > 0 && "chunked".equalsIgnoreCase(encodings[len - 1].getName())) {
            if (conn.isResponseAvailable(conn.getSoTimeout())) {
               result = new ChunkedInputStream((InputStream)is, this);
            } else {
               if (this.isStrictMode()) {
                  throw new HttpException("Chunk-encoded body declared but not sent");
               }

               LOG.warn("Chunk-encoded body missing");
            }
         } else {
            LOG.info("Response content is not chunk-encoded");
            this.setConnectionCloseForced(true);
            result = is;
         }
      } else {
         int expectedLength = this.getResponseContentLength();
         if (expectedLength == -1) {
            if (canResponseHaveBody(this.statusLine.getStatusCode())) {
               Header connectionHeader = this.responseHeaders.getFirstHeader("Connection");
               String connectionDirective = null;
               if (connectionHeader != null) {
                  connectionDirective = connectionHeader.getValue();
               }

               if (this.isHttp11() && !"close".equalsIgnoreCase(connectionDirective)) {
                  LOG.info("Response content length is not known");
                  this.setConnectionCloseForced(true);
               }

               result = is;
            }
         } else {
            result = new ContentLengthInputStream((InputStream)is, expectedLength);
         }
      }

      if (result != null) {
         result = new AutoCloseInputStream((InputStream)result, new ResponseConsumedWatcher() {
            public void responseConsumed() {
               HttpMethodBase.this.responseBodyConsumed();
            }
         });
      }

      return (InputStream)result;
   }

   protected void readResponseHeaders(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter HttpMethodBase.readResponseHeaders(HttpState,HttpConnection)");
      this.getResponseHeaderGroup().clear();
      Header[] headers = HttpParser.parseHeaders(conn.getResponseInputStream());
      if (Wire.HEADER_WIRE.enabled()) {
         for(int i = 0; i < headers.length; ++i) {
            Wire.HEADER_WIRE.input(headers[i].toExternalForm());
         }
      }

      this.getResponseHeaderGroup().setHeaders(headers);
   }

   protected void readStatusLine(HttpState state, HttpConnection conn) throws IOException, HttpRecoverableException, HttpException {
      LOG.trace("enter HttpMethodBase.readStatusLine(HttpState, HttpConnection)");

      String s;
      for(s = conn.readLine(); s != null && !StatusLine.startsWithHTTP(s); s = conn.readLine()) {
         if (Wire.HEADER_WIRE.enabled()) {
            Wire.HEADER_WIRE.input(s + "\r\n");
         }
      }

      if (s == null) {
         throw new HttpRecoverableException("Error in parsing the status  line from the response: unable to find line starting with \"HTTP\"");
      } else {
         if (Wire.HEADER_WIRE.enabled()) {
            Wire.HEADER_WIRE.input(s + "\r\n");
         }

         this.statusLine = new StatusLine(s);
         String httpVersion = this.statusLine.getHttpVersion();
         if (httpVersion.equals("HTTP/1.0")) {
            this.http11 = false;
         } else if (httpVersion.equals("HTTP/1.1")) {
            this.http11 = true;
         } else {
            if (!httpVersion.equals("HTTP")) {
               throw new HttpException("Unrecognized server protocol: '" + httpVersion + "'");
            }

            this.http11 = false;
         }

      }
   }

   protected void writeRequest(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter HttpMethodBase.writeRequest(HttpState, HttpConnection)");
      this.writeRequestLine(state, conn);
      this.writeRequestHeaders(state, conn);
      conn.writeLine();
      conn.flushRequestOutputStream();
      if (Wire.HEADER_WIRE.enabled()) {
         Wire.HEADER_WIRE.output("\r\n");
      }

      Header expectheader = this.getRequestHeader("Expect");
      String expectvalue = null;
      if (expectheader != null) {
         expectvalue = expectheader.getValue();
      }

      if (expectvalue != null && expectvalue.compareToIgnoreCase("100-continue") == 0) {
         if (this.isHttp11()) {
            label96: {
               int readTimeout = conn.getSoTimeout();

               try {
                  conn.setSoTimeout(3000);
                  this.readStatusLine(state, conn);
                  this.processStatusLine(state, conn);
                  this.readResponseHeaders(state, conn);
                  this.processResponseHeaders(state, conn);
                  if (this.statusLine.getStatusCode() == 100) {
                     this.statusLine = null;
                     LOG.debug("OK to continue received");
                     break label96;
                  }
               } catch (InterruptedIOException var10) {
                  this.removeRequestHeader("Expect");
                  LOG.info("100 (continue) read timeout. Resume sending the request");
                  break label96;
               } finally {
                  conn.setSoTimeout(readTimeout);
               }

               return;
            }
         } else {
            this.removeRequestHeader("Expect");
            LOG.info("'Expect: 100-continue' handshake is only supported by HTTP/1.1 or higher");
         }
      }

      this.writeRequestBody(state, conn);
      conn.flushRequestOutputStream();
   }

   protected boolean writeRequestBody(HttpState state, HttpConnection conn) throws IOException, HttpException {
      return true;
   }

   protected void writeRequestHeaders(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter HttpMethodBase.writeRequestHeaders(HttpState,HttpConnection)");
      this.addRequestHeaders(state, conn);
      Header[] headers = this.getRequestHeaders();

      for(int i = 0; i < headers.length; ++i) {
         String s = headers[i].toExternalForm();
         if (Wire.HEADER_WIRE.enabled()) {
            Wire.HEADER_WIRE.output(s);
         }

         conn.print(s);
      }

   }

   protected void writeRequestLine(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter HttpMethodBase.writeRequestLine(HttpState, HttpConnection)");
      String requestLine = this.getRequestLine(conn);
      if (Wire.HEADER_WIRE.enabled()) {
         Wire.HEADER_WIRE.output(requestLine);
      }

      conn.print(requestLine);
   }

   private String getRequestLine(HttpConnection conn) {
      return generateRequestLine(conn, this.getName(), this.getPath(), this.getQueryString(), this.getHttpVersion());
   }

   private String getHttpVersion() {
      return this.http11 ? "HTTP/1.1" : "HTTP/1.0";
   }

   private static boolean canResponseHaveBody(int status) {
      LOG.trace("enter HttpMethodBase.canResponseHaveBody(int)");
      boolean result = true;
      if (status >= 100 && status <= 199 || status == 204 || status == 304) {
         result = false;
      }

      return result;
   }

   private boolean processAuthenticationResponse(HttpState state, HttpConnection conn) {
      LOG.trace("enter HttpMethodBase.processAuthenticationResponse(HttpState, HttpConnection)");
      if (this.proxyAuthScheme instanceof NTLMScheme) {
         this.removeRequestHeader("Proxy-Authorization");
      }

      if (this.authScheme instanceof NTLMScheme) {
         this.removeRequestHeader("Authorization");
      }

      int statusCode = this.statusLine.getStatusCode();
      Header[] challenges = null;
      Set realmsUsed = null;
      String host = null;
      switch(statusCode) {
      case 401:
         challenges = this.getResponseHeaderGroup().getHeaders("WWW-Authenticate");
         realmsUsed = this.realms;
         host = conn.getVirtualHost();
         if (host == null) {
            host = conn.getHost();
         }
         break;
      case 407:
         challenges = this.getResponseHeaderGroup().getHeaders("Proxy-Authenticate");
         realmsUsed = this.proxyRealms;
         host = conn.getProxyHost();
      }

      boolean authenticated = false;
      if (challenges.length > 0) {
         AuthScheme authscheme = null;

         try {
            authscheme = HttpAuthenticator.selectAuthScheme(challenges);
         } catch (MalformedChallengeException var13) {
            if (LOG.isErrorEnabled()) {
               LOG.error(var13.getMessage(), var13);
            }

            return true;
         } catch (UnsupportedOperationException var14) {
            if (LOG.isErrorEnabled()) {
               LOG.error(var14.getMessage(), var14);
            }

            return true;
         }

         StringBuffer buffer = new StringBuffer();
         buffer.append(host);
         buffer.append('#');
         buffer.append(authscheme.getID());
         String realm = buffer.toString();
         if (realmsUsed.contains(realm)) {
            if (LOG.isInfoEnabled()) {
               buffer = new StringBuffer();
               buffer.append("Already tried to authenticate with '");
               buffer.append(authscheme.getRealm());
               buffer.append("' authentication realm at ");
               buffer.append(host);
               buffer.append(", but still receiving: ");
               buffer.append(this.statusLine.toString());
               LOG.info(buffer.toString());
            }

            return true;
         }

         realmsUsed.add(realm);

         try {
            switch(statusCode) {
            case 401:
               this.removeRequestHeader("Authorization");
               authenticated = HttpAuthenticator.authenticate(authscheme, this, conn, state);
               this.realm = authscheme.getRealm();
               this.authScheme = authscheme;
               break;
            case 407:
               this.removeRequestHeader("Proxy-Authorization");
               authenticated = HttpAuthenticator.authenticateProxy(authscheme, this, conn, state);
               this.proxyRealm = authscheme.getRealm();
               this.proxyAuthScheme = authscheme;
            }
         } catch (AuthenticationException var12) {
            LOG.warn(var12.getMessage());
            return true;
         }

         if (!authenticated) {
            LOG.debug("HttpMethodBase.execute(): Server demands authentication credentials, but none are available, so aborting.");
         } else {
            LOG.debug("HttpMethodBase.execute(): Server demanded authentication credentials, will try again.");
         }
      }

      return !authenticated;
   }

   public String getProxyAuthenticationRealm() {
      return this.proxyRealm;
   }

   public String getAuthenticationRealm() {
      return this.realm;
   }

   private void processRequest(HttpState state, HttpConnection connection) throws HttpException, IOException {
      LOG.trace("enter HttpMethodBase.processRequest(HttpState, HttpConnection)");
      int execCount = 0;
      boolean requestSent = false;

      while(true) {
         ++execCount;
         requestSent = false;
         if (LOG.isTraceEnabled()) {
            LOG.trace("Attempt number " + execCount + " to process request");
         }

         try {
            if (!connection.isOpen()) {
               LOG.debug("Opening the connection.");
               connection.open();
            }

            this.writeRequest(state, connection);
            requestSent = true;
            this.readResponse(state, connection);
            this.used = true;
            return;
         } catch (HttpRecoverableException var6) {
            if (LOG.isDebugEnabled()) {
               LOG.debug("Closing the connection.");
            }

            connection.close();
            LOG.info("Recoverable exception caught when processing request");
            ++this.recoverableExceptionCount;
            if (!this.getMethodRetryHandler().retryMethod(this, connection, var6, execCount, requestSent)) {
               LOG.warn("Recoverable exception caught but MethodRetryHandler.retryMethod() returned false, rethrowing exception");
               this.doneWithConnection = true;
               throw var6;
            }
         } catch (IOException var7) {
            connection.close();
            this.doneWithConnection = true;
            throw var7;
         } catch (RuntimeException var8) {
            connection.close();
            this.doneWithConnection = true;
            throw var8;
         }
      }
   }

   protected static String getContentCharSet(Header contentheader) {
      LOG.trace("enter getContentCharSet( Header contentheader )");
      String charset = null;
      if (contentheader != null) {
         try {
            HeaderElement[] values = contentheader.getValues();
            if (values.length == 1) {
               NameValuePair param = values[0].getParameterByName("charset");
               if (param != null) {
                  charset = param.getValue();
               }
            }
         } catch (HttpException var4) {
            LOG.error(var4);
         }
      }

      if (charset == null) {
         if (LOG.isDebugEnabled()) {
            LOG.debug("Default charset used: ISO-8859-1");
         }

         charset = "ISO-8859-1";
      }

      return charset;
   }

   public String getRequestCharSet() {
      return getContentCharSet(this.getRequestHeader("Content-Type"));
   }

   public String getResponseCharSet() {
      return getContentCharSet(this.getResponseHeader("Content-Type"));
   }

   public int getRecoverableExceptionCount() {
      return this.recoverableExceptionCount;
   }

   protected void responseBodyConsumed() {
      this.responseStream = null;
      if (this.responseConnection != null) {
         this.responseConnection.setLastResponseInputStream((InputStream)null);
         if (this.shouldCloseConnection(this.responseConnection)) {
            this.responseConnection.close();
         }
      }

      this.connectionCloseForced = false;
      this.doneWithConnection = true;
      if (!this.inExecute) {
         this.ensureConnectionRelease();
      }

   }

   private void ensureConnectionRelease() {
      if (this.responseConnection != null) {
         this.responseConnection.releaseConnection();
         this.responseConnection = null;
      }

   }

   public HostConfiguration getHostConfiguration() {
      return this.hostConfiguration;
   }

   public void setHostConfiguration(HostConfiguration hostConfiguration) {
      this.hostConfiguration = hostConfiguration;
   }

   public MethodRetryHandler getMethodRetryHandler() {
      if (this.methodRetryHandler == null) {
         this.methodRetryHandler = new DefaultMethodRetryHandler();
      }

      return this.methodRetryHandler;
   }

   public void setMethodRetryHandler(MethodRetryHandler handler) {
      this.methodRetryHandler = handler;
   }

   protected void fakeResponse(StatusLine statusline, HeaderGroup responseheaders, InputStream responseStream) {
      this.used = true;
      this.statusLine = statusline;
      this.responseHeaders = responseheaders;
      this.responseBody = null;
      this.responseStream = responseStream;
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$HttpMethodBase == null ? (class$org$apache$commons$httpclient$HttpMethodBase = class$("org.apache.commons.httpclient.HttpMethodBase")) : class$org$apache$commons$httpclient$HttpMethodBase);
      String agent = null;

      try {
         agent = System.getProperty("httpclient.useragent");
      } catch (SecurityException var2) {
      }

      if (agent == null) {
         agent = "Jakarta Commons-HttpClient/2.0.2";
      }

      USER_AGENT = new Header("User-Agent", agent);
   }
}

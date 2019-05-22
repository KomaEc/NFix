package org.apache.commons.httpclient;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConnectMethod extends HttpMethodBase {
   public static final String NAME = "CONNECT";
   private static final Log LOG;
   private HttpMethod method;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$ConnectMethod;

   public ConnectMethod(HttpMethod method) {
      LOG.trace("enter ConnectMethod(HttpMethod)");
      this.method = method;
   }

   public String getName() {
      return "CONNECT";
   }

   protected void addAuthorizationRequestHeader(HttpState state, HttpConnection conn) throws IOException, HttpException {
   }

   protected void addContentLengthRequestHeader(HttpState state, HttpConnection conn) throws IOException, HttpException {
   }

   protected void addCookieRequestHeader(HttpState state, HttpConnection conn) throws IOException, HttpException {
   }

   protected void addRequestHeaders(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter ConnectMethod.addRequestHeaders(HttpState, HttpConnection)");
      this.addUserAgentRequestHeader(state, conn);
      this.addHostRequestHeader(state, conn);
      this.addProxyAuthorizationRequestHeader(state, conn);
      this.addProxyConnectionHeader(state, conn);
   }

   public int execute(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter ConnectMethod.execute(HttpState, HttpConnection)");
      int code = super.execute(state, conn);
      LOG.debug("CONNECT status code " + code);
      if (code >= 200 && code < 300) {
         conn.tunnelCreated();
         code = this.method.execute(state, conn);
      } else {
         LOG.debug("CONNECT failed, fake the response for the original method");
         if (this.method instanceof HttpMethodBase) {
            ((HttpMethodBase)this.method).fakeResponse(this.getStatusLine(), this.getResponseHeaderGroup(), this.getResponseStream());
         } else {
            this.releaseConnection();
         }
      }

      return code;
   }

   protected void writeRequestLine(HttpState state, HttpConnection conn) throws IOException, HttpException {
      int port = conn.getPort();
      if (port == -1) {
         port = conn.getProtocol().getDefaultPort();
      }

      StringBuffer buffer = new StringBuffer();
      buffer.append(this.getName());
      buffer.append(' ');
      buffer.append(conn.getHost());
      if (port > -1) {
         buffer.append(':');
         buffer.append(port);
      }

      buffer.append(" HTTP/1.1");
      String line = buffer.toString();
      conn.printLine(line);
      if (Wire.HEADER_WIRE.enabled()) {
         Wire.HEADER_WIRE.output(line);
      }

   }

   protected boolean shouldCloseConnection(HttpConnection conn) {
      if (this.getStatusCode() == 200) {
         Header connectionHeader = null;
         if (!conn.isTransparent()) {
            connectionHeader = this.getResponseHeader("proxy-connection");
         }

         if (connectionHeader == null) {
            connectionHeader = this.getResponseHeader("connection");
         }

         if (connectionHeader != null && connectionHeader.getValue().equalsIgnoreCase("close") && LOG.isWarnEnabled()) {
            LOG.warn("Invalid header encountered '" + connectionHeader.toExternalForm() + "' in response " + this.getStatusLine().toString());
         }

         return false;
      } else {
         return super.shouldCloseConnection(conn);
      }
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$ConnectMethod == null ? (class$org$apache$commons$httpclient$ConnectMethod = class$("org.apache.commons.httpclient.ConnectMethod")) : class$org$apache$commons$httpclient$ConnectMethod);
   }
}

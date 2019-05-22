package org.apache.commons.httpclient;

import java.util.ArrayList;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.HttpAuthenticator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** @deprecated */
public class Authenticator {
   private static final Log LOG;
   public static final String WWW_AUTH = "WWW-Authenticate";
   public static final String WWW_AUTH_RESP = "Authorization";
   public static final String PROXY_AUTH = "Proxy-Authenticate";
   public static final String PROXY_AUTH_RESP = "Proxy-Authorization";
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$Authenticator;

   /** @deprecated */
   public static boolean authenticate(HttpMethod method, HttpState state) throws HttpException, UnsupportedOperationException {
      LOG.trace("enter Authenticator.authenticate(HttpMethod, HttpState)");
      return authenticate(method, state, false);
   }

   /** @deprecated */
   public static boolean authenticateProxy(HttpMethod method, HttpState state) throws HttpException, UnsupportedOperationException {
      LOG.trace("enter Authenticator.authenticateProxy(HttpMethod, HttpState)");
      return authenticate(method, state, true);
   }

   private static boolean authenticate(HttpMethod method, HttpState state, boolean proxy) throws HttpException, UnsupportedOperationException {
      LOG.trace("enter Authenticator.authenticate(HttpMethod, HttpState, Header, String)");
      return authenticate(method, (HttpConnection)null, state, proxy);
   }

   private static boolean authenticate(HttpMethod method, HttpConnection conn, HttpState state, boolean proxy) throws HttpException, UnsupportedOperationException {
      String challengeheader = proxy ? "Proxy-Authenticate" : "WWW-Authenticate";
      Header[] headers = method.getResponseHeaders();
      ArrayList headerlist = new ArrayList();

      for(int i = 0; i < headers.length; ++i) {
         Header header = headers[i];
         if (header.getName().equalsIgnoreCase(challengeheader)) {
            headerlist.add(header);
         }
      }

      headers = (Header[])headerlist.toArray(new Header[headerlist.size()]);
      headerlist = null;
      if (headers.length == 0) {
         if (state.isAuthenticationPreemptive()) {
            LOG.debug("Preemptively sending default basic credentials");
            if (proxy) {
               return HttpAuthenticator.authenticateProxyDefault(method, conn, state);
            } else {
               return HttpAuthenticator.authenticateDefault(method, conn, state);
            }
         } else {
            return false;
         }
      } else {
         AuthScheme authscheme = HttpAuthenticator.selectAuthScheme(headers);
         if (LOG.isDebugEnabled()) {
            LOG.debug("Using " + authscheme.getSchemeName() + " authentication scheme");
         }

         if (proxy) {
            return HttpAuthenticator.authenticateProxy(authscheme, method, conn, state);
         } else {
            return HttpAuthenticator.authenticate(authscheme, method, conn, state);
         }
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$Authenticator == null ? (class$org$apache$commons$httpclient$Authenticator = class$("org.apache.commons.httpclient.Authenticator")) : class$org$apache$commons$httpclient$Authenticator);
   }
}

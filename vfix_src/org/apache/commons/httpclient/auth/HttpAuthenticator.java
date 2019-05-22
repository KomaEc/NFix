package org.apache.commons.httpclient.auth;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class HttpAuthenticator {
   private static final Log LOG;
   public static final String WWW_AUTH = "WWW-Authenticate";
   public static final String WWW_AUTH_RESP = "Authorization";
   public static final String PROXY_AUTH = "Proxy-Authenticate";
   public static final String PROXY_AUTH_RESP = "Proxy-Authorization";
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$auth$HttpAuthenticator;

   public static AuthScheme selectAuthScheme(Header[] challenges) throws MalformedChallengeException {
      LOG.trace("enter HttpAuthenticator.selectAuthScheme(Header[])");
      if (challenges == null) {
         throw new IllegalArgumentException("Array of challenges may not be null");
      } else if (challenges.length == 0) {
         throw new IllegalArgumentException("Array of challenges may not be empty");
      } else {
         String challenge = null;
         Map challengemap = new HashMap(challenges.length);

         for(int i = 0; i < challenges.length; ++i) {
            challenge = challenges[i].getValue();
            String s = AuthChallengeParser.extractScheme(challenge);
            challengemap.put(s, challenge);
         }

         challenge = (String)challengemap.get("ntlm");
         if (challenge != null) {
            return new NTLMScheme(challenge);
         } else {
            challenge = (String)challengemap.get("digest");
            if (challenge != null) {
               return new DigestScheme(challenge);
            } else {
               challenge = (String)challengemap.get("basic");
               if (challenge != null) {
                  return new BasicScheme(challenge);
               } else {
                  throw new UnsupportedOperationException("Authentication scheme(s) not supported: " + challengemap.toString());
               }
            }
         }
      }
   }

   private static boolean doAuthenticateDefault(HttpMethod method, HttpConnection conn, HttpState state, boolean proxy) throws AuthenticationException {
      if (method == null) {
         throw new IllegalArgumentException("HTTP method may not be null");
      } else if (state == null) {
         throw new IllegalArgumentException("HTTP state may not be null");
      } else {
         String host = null;
         if (conn != null) {
            host = proxy ? conn.getProxyHost() : conn.getHost();
         }

         Credentials credentials = proxy ? state.getProxyCredentials((String)null, host) : state.getCredentials((String)null, host);
         if (credentials == null) {
            if (LOG.isWarnEnabled()) {
               LOG.warn("Default credentials for " + host + " not available");
            }

            return false;
         } else if (!(credentials instanceof UsernamePasswordCredentials)) {
            throw new AuthenticationException("Credentials cannot be used for basic authentication: " + credentials.toString());
         } else {
            String auth = BasicScheme.authenticate((UsernamePasswordCredentials)credentials);
            if (auth != null) {
               String s = proxy ? "Proxy-Authorization" : "Authorization";
               method.setRequestHeader(s, auth);
               return true;
            } else {
               return false;
            }
         }
      }
   }

   public static boolean authenticateDefault(HttpMethod method, HttpConnection conn, HttpState state) throws AuthenticationException {
      LOG.trace("enter HttpAuthenticator.authenticateDefault(HttpMethod, HttpConnection, HttpState)");
      return doAuthenticateDefault(method, conn, state, false);
   }

   public static boolean authenticateProxyDefault(HttpMethod method, HttpConnection conn, HttpState state) throws AuthenticationException {
      LOG.trace("enter HttpAuthenticator.authenticateProxyDefault(HttpMethod, HttpState)");
      return doAuthenticateDefault(method, conn, state, true);
   }

   private static boolean doAuthenticate(AuthScheme authscheme, HttpMethod method, HttpConnection conn, HttpState state, boolean proxy) throws AuthenticationException {
      if (authscheme == null) {
         throw new IllegalArgumentException("Authentication scheme may not be null");
      } else if (method == null) {
         throw new IllegalArgumentException("HTTP method may not be null");
      } else if (state == null) {
         throw new IllegalArgumentException("HTTP state may not be null");
      } else {
         String host = null;
         if (conn != null) {
            if (proxy) {
               host = conn.getProxyHost();
            } else {
               host = conn.getVirtualHost();
               if (host == null) {
                  host = conn.getHost();
               }
            }
         }

         String realm = authscheme.getRealm();
         if (LOG.isDebugEnabled()) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("Authenticating with the ");
            if (realm == null) {
               buffer.append("default");
            } else {
               buffer.append('\'');
               buffer.append(realm);
               buffer.append('\'');
            }

            buffer.append(" authentication realm at ");
            buffer.append(host);
            LOG.debug(buffer.toString());
         }

         if (realm == null) {
            realm = host;
         }

         Credentials credentials = proxy ? state.getProxyCredentials(realm, host) : state.getCredentials(realm, host);
         if (credentials == null) {
            throw new AuthenticationException("No credentials available for the '" + authscheme.getRealm() + "' authentication realm at " + host);
         } else {
            String auth = authscheme.authenticate(credentials, method.getName(), method.getPath());
            if (auth != null) {
               String s = proxy ? "Proxy-Authorization" : "Authorization";
               method.setRequestHeader(s, auth);
               return true;
            } else {
               return false;
            }
         }
      }
   }

   public static boolean authenticate(AuthScheme authscheme, HttpMethod method, HttpConnection conn, HttpState state) throws AuthenticationException {
      LOG.trace("enter HttpAuthenticator.authenticate(AuthScheme, HttpMethod, HttpConnection, HttpState)");
      return doAuthenticate(authscheme, method, conn, state, false);
   }

   public static boolean authenticateProxy(AuthScheme authscheme, HttpMethod method, HttpConnection conn, HttpState state) throws AuthenticationException {
      LOG.trace("enter HttpAuthenticator.authenticateProxy(AuthScheme, HttpMethod, HttpState)");
      return doAuthenticate(authscheme, method, conn, state, true);
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$auth$HttpAuthenticator == null ? (class$org$apache$commons$httpclient$auth$HttpAuthenticator = class$("org.apache.commons.httpclient.auth.HttpAuthenticator")) : class$org$apache$commons$httpclient$auth$HttpAuthenticator);
   }
}
